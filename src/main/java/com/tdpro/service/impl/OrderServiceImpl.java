package com.tdpro.service.impl;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.*;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.*;
import com.tdpro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private PCartMapper cartMapper;
    @Autowired
    private POrderMapper orderMapper;
    @Autowired
    private PGoodsMapper goodsMapper;
    @Autowired
    private PUserSiteMapper userSiteMapper;
    @Autowired
    private PGoodsExchangeMapper exchangeMapper;
    @Autowired
    private PUserVoucherMapper userVoucherMapper;
    @Autowired
    private PGoodsSuitMapper goodsSuitMapper;
    @Autowired
    private POrderVoucherMapper orderVoucherMapper;
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response insertOrder(OrderCartETD orderCartETD) {
        if(null == orderCartETD.getGoodsId() || null ==  orderCartETD.getSuitList() || orderCartETD.getSuitList().size()  <= 0){
           return ResponseUtils.errorRes("操作异常");
        }
        Long uid = orderCartETD.getUid();
        Long goodsId = orderCartETD.getGoodsId();
        GoodsETD goodsWhere = new GoodsETD();
        goodsWhere.setId(goodsId);
        GoodsETD goodsInfo = goodsMapper.selectInfo(goodsWhere);
        if(null == goodsInfo){
            return  ResponseUtils.errorRes("产品已下架");
        }
        if(goodsInfo.getRepertory().compareTo(new Integer(0)) < 0){
            return ResponseUtils.errorRes("库存不足");
        }
        if(null == orderCartETD.getSiteId()){
            return ResponseUtils.errorRes("请选择收货地址");
        }
        PUserSite site = userSiteMapper.selectByPrimaryKey(orderCartETD.getSiteId());
        if(null == site || !site.getUid().equals(orderCartETD.getUid())){
            return ResponseUtils.errorRes("收货地址异常");
        }
        int orderNumber = 0;
        if(null != orderCartETD.getSuitList() && orderCartETD.getSuitList().size() > 0) {
            List<GoodsSuitETD> suitList = orderCartETD.getSuitList();
            for (GoodsSuitETD suit : suitList) {
                int num = (null == suit.getNumber() || suit.getNumber() == 0) ? 1 : suit.getNumber();
                orderNumber = orderNumber+num;
            }
        }else{
            orderNumber = 1;
        }
        List<PUserVoucher> voucherList = null;
        BigDecimal discountAmount = new BigDecimal("0");
        switch (goodsInfo.getZoneType().intValue()){
            case 0:
                if(null != orderCartETD.getVoucherId()){
                    GoodsExchangeETD goodsVoucher = exchangeMapper.selectByGoodsIdAndVoucherId(goodsId,orderCartETD.getVoucherId());
                    if(null == goodsVoucher){
                        return ResponseUtils.errorRes("该商品不能使用当前券");
                    }
                    if(goodsVoucher.getNumber().compareTo(new Integer(0)) > 0){
                        UserVoucherETD voucherETD = new UserVoucherETD();
                        voucherETD.setUid(uid);
                        voucherETD.setVoucherId(goodsVoucher.getId());
                        voucherETD.setNumber(goodsVoucher.getNumber());
                        voucherList = userVoucherMapper.countByUidAndVoucherId(voucherETD);
                        if(null == voucherList || voucherList.size() < goodsVoucher.getNumber().intValue()){
                            return ResponseUtils.errorRes("您的"+goodsVoucher.getVoucherName()+"数量不足");
                        }
                        BigDecimal needNum = new BigDecimal(goodsVoucher.getNumber().toString());
                        BigDecimal faceValue = goodsVoucher.getFaceValue();
                        discountAmount =faceValue.multiply(needNum).setScale(2,BigDecimal.ROUND_DOWN);
                    }
                }
                break;
            case 2:
                List<GoodsExchangeETD> goodsExchangeList = exchangeMapper.selectListByGoodsId(goodsId);
                if(null == goodsExchangeList || goodsExchangeList.size() <= 0){
                    return ResponseUtils.errorRes("商品配置异常");
                }
                for (GoodsExchangeETD exchange:goodsExchangeList){
                    if(exchange.getNumber().compareTo(new Integer(0)) <= 0){
                        return ResponseUtils.errorRes("商品兑换配置异常");
                    }
                    Long voucherId = exchange.getVoucherId();
                    int needNum = exchange.getNumber()*orderNumber;
                    UserVoucherETD voucherETD = new UserVoucherETD();
                    voucherETD.setUid(uid);
                    voucherETD.setVoucherId(voucherId);
                    voucherETD.setNumber(needNum);
                    voucherList = userVoucherMapper.countByUidAndVoucherId(voucherETD);
                    if(null == voucherList || voucherList.size() < needNum){
                        return ResponseUtils.errorRes("您的"+exchange.getVoucherName()+"数量不足");
                    }
                }
                break;
            default:
                return ResponseUtils.errorRes("商品配置异常");
        }
        if(goodsInfo.getRepertory().compareTo(new Integer(orderNumber)) < 0){
            return ResponseUtils.errorRes("库存不足");
        }
        POrder orderADD = getAddOrder(orderCartETD,goodsInfo, site,orderNumber);
        Long orderId = orderADD.getId();
        BigDecimal totalPrice = new BigDecimal("0");
        switch (goodsInfo.getIsSuit().intValue()){
            case 1:
                List<GoodsSuitETD> suitList = orderCartETD.getSuitList();
                for (GoodsSuitETD suit:suitList){
                    int num = (null == suit.getNumber() || suit.getNumber() == 0) ? 1 : suit.getNumber();
                    if(null == suit.getId()) {
                        throw new RuntimeException("规格选择错误");
                    }
                    PGoodsSuit suitInfo = goodsSuitMapper.selectByPrimaryKey(suit.getId());
                    if (null == suitInfo || !suitInfo.getGoodsId().equals(goodsId)) {
                        throw new RuntimeException("规格选择错误");
                    }
                    addCard(uid,goodsInfo, orderId, num, suitInfo);
                    totalPrice = totalPrice.add(goodsInfo.getPrice().multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_DOWN));
                }
                break;
            default:
                addCard(uid,goodsInfo, orderId, orderNumber, null);
                totalPrice = totalPrice.add(goodsInfo.getPrice().multiply(new BigDecimal(orderNumber)).setScale(2,BigDecimal.ROUND_DOWN));
                break;
        }
        if(null != voucherList && voucherList.size() > 0){
            for (PUserVoucher userVoucher:voucherList){
                POrderVoucher orderVoucherADD = new POrderVoucher();
                orderVoucherADD.setOrderId(orderId);
                orderVoucherADD.setUid(uid);
                orderVoucherADD.setUserVoucherId(userVoucher.getId());
                if(orderVoucherMapper.insertSelective(orderVoucherADD) == 0){
                    throw new RuntimeException("券绑定失败");
                }
            }
        }
        BigDecimal realPrice = totalPrice.subtract(discountAmount);
        if(realPrice.compareTo(new BigDecimal("0")) < 0){
            realPrice = new BigDecimal("0");
        }
        POrder orderUPD = new POrder();
        orderUPD.setId(orderId);
        orderUPD.setTotalPrice(totalPrice);
        orderUPD.setRealPrice(realPrice);
        if(orderMapper.updateByPrimaryKeySelective(orderUPD) == 0){
            throw new RuntimeException("修改订单失败");
        }
        return ResponseUtils.successRes(orderId);
    }

    private void addCard(Long uid,GoodsETD goodsInfo, Long orderId, int num, PGoodsSuit suitInfo) {
        PCart cartADD= new PCart();
        cartADD.setUid(uid);
        cartADD.setOrderId(orderId);
        cartADD.setGoodsId(goodsInfo.getId());
        cartADD.setPrice(goodsInfo.getPrice());
        cartADD.setNumber(num);
        cartADD.setGoodsName(goodsInfo.getGoodsName());
        cartADD.setCreateTime(new Date());
        if(null != suitInfo) {
            cartADD.setSuitId(suitInfo.getId());
            cartADD.setSuitName(suitInfo.getExplain());
        }
        if(0 == cartMapper.insertSelective(cartADD)){
            throw new RuntimeException("添加购物车失败");
        }
    }

    private POrder getAddOrder(OrderCartETD orderCartETD, GoodsETD goodsInfo, PUserSite site,int orderNumber) {
        Long goodsId = goodsInfo.getId();
        Long uid = site.getUid();
        POrder orderADD = new POrder();
        orderADD.setUid(uid);
        orderADD.setOrderNo(createOrderNo(uid));
        orderADD.setGoodsId(goodsId);
        orderADD.setGoodsName(goodsInfo.getGoodsName());
        orderADD.setZoneType(goodsInfo.getZoneType());
        orderADD.setIsSuit(goodsInfo.getIsSuit());
        orderADD.setState(0);
        orderADD.setCreateTime(new Date());
        orderADD.setReceiptName(site.getName());
        orderADD.setReceiptPhone(site.getPhone());
        orderADD.setReceiptSite(site.getSite());
        orderADD.setNumber(orderNumber);
        if(null != orderCartETD.getUserNote()){
            orderADD.setUserNote(orderCartETD.getUserNote());
        }
        int addOrder = orderMapper.insertSelective(orderADD);
        if(0 == addOrder){
            throw new RuntimeException("添加订单失败");
        }
        return orderADD;
    }

    private String createOrderNo(Long uid) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(uid).append(System.currentTimeMillis());
        return stringBuffer.toString();
    }
}
