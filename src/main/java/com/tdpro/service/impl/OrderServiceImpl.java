package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.*;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.*;
import com.tdpro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private POrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserSiteService userSiteService;
    @Autowired
    private GoodsExchangeService exchangeService;
    @Autowired
    private UserVoucherService userVoucherService;
    @Autowired
    private GoodsSuitService goodsSuitService;
    @Autowired
    private OrderVoucherService orderVoucherService;
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response insertOrder(OrderCartETD orderCartETD) {
        if(null == orderCartETD.getGoodsId() || null ==  orderCartETD.getSuitList() || orderCartETD.getSuitList().size()  <= 0){
           return ResponseUtils.errorRes("操作异常");
        }
        Long uid = orderCartETD.getUid();
        Long goodsId = orderCartETD.getGoodsId();
        GoodsETD goodsInfo = goodsService.selectInfo(goodsId);
        if(null == goodsInfo){
            return  ResponseUtils.errorRes("产品已下架");
        }
        if(goodsInfo.getRepertory().compareTo(new Integer(0)) < 0){
            return ResponseUtils.errorRes("库存不足");
        }
        if(null == orderCartETD.getSiteId()){
            return ResponseUtils.errorRes("请选择收货地址");
        }
        PUserSite site = userSiteService.findById(orderCartETD.getSiteId());
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
        List<PUserVoucher> voucherList = new ArrayList<>();
        BigDecimal discountAmount = new BigDecimal("0");
        switch (goodsInfo.getZoneType().intValue()){
            case 0:
                if(null != orderCartETD.getVoucherId() && !orderCartETD.getVoucherId().equals(new Long(0))){
                    GoodsExchangeETD goodsVoucher = exchangeService.selectByGoodsIdAndVoucherId(goodsId,orderCartETD.getVoucherId());
                    if(null == goodsVoucher){
                        return ResponseUtils.errorRes("该商品不能使用当前券");
                    }
                    if(goodsVoucher.getNumber().compareTo(new Integer(0)) > 0){
                        voucherList = userVoucherService.selectByUidAndVoucherId(uid,goodsVoucher.getVoucherId(),goodsVoucher.getNumber());
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
                List<GoodsExchangeETD> goodsExchangeList = exchangeService.selectListByGoodsId(goodsId);
                if(null == goodsExchangeList || goodsExchangeList.size() <= 0){
                    return ResponseUtils.errorRes("商品配置异常");
                }
                for (GoodsExchangeETD exchange:goodsExchangeList){
                    if(exchange.getNumber().compareTo(new Integer(0)) <= 0){
                        return ResponseUtils.errorRes("商品兑换配置异常");
                    }
                    Long voucherId = exchange.getVoucherId();
                    int needNum = exchange.getNumber()*orderNumber;
                    List<PUserVoucher> voucherListOne = userVoucherService.selectByUidAndVoucherId(uid,voucherId,needNum);
                    if(null == voucherListOne || voucherListOne.size() < needNum){
                        return ResponseUtils.errorRes("您的"+exchange.getVoucherName()+"数量不足");
                    }
                    voucherList.addAll(voucherListOne);
                }
                discountAmount = goodsInfo.getPrice().multiply(new BigDecimal(orderNumber));
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
                    PGoodsSuit suitInfo = goodsSuitService.findById(suit.getId());
                    if (null == suitInfo || !suitInfo.getGoodsId().equals(goodsId)) {
                        throw new RuntimeException("规格选择错误");
                    }
                    if(!cartService.insertCart(uid,goodsInfo, orderId, num, suitInfo)){
                        throw new RuntimeException("添加购物车失败");
                    }
                    totalPrice = totalPrice.add(goodsInfo.getPrice().multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_DOWN));
                }
                break;
            default:
                if(!cartService.insertCart(uid,goodsInfo, orderId, orderNumber, null)){
                    throw new RuntimeException("添加购物车失败");
                }
                totalPrice = totalPrice.add(goodsInfo.getPrice().multiply(new BigDecimal(orderNumber)).setScale(2,BigDecimal.ROUND_DOWN));
                break;
        }
        if(null != voucherList && voucherList.size() > 0){
            for (PUserVoucher userVoucher:voucherList){
                if(!orderVoucherService.insertVoucher(orderId,uid,userVoucher.getId())){
                    throw new RuntimeException("券绑定失败");
                }
            }
            if(!userVoucherService.updateUserVoucherIsLock(voucherList)){
                throw new RuntimeException("优惠券绑定失败");
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

    @Override
    public POrder findOrderById(Long id) {
        return orderMapper.selectByPrimaryKey(id);
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

    @Override
    public Boolean updateOrderIsPay(Long id, PayType payType,String wxOrderNo,BigDecimal callbackPrice) {
        POrder orderUPD = new POrder();
        orderUPD.setId(id);
        orderUPD.setState(1);
        orderUPD.setPayTime(new Date());
        orderUPD.setPayType(payType.getType());
        if(StringUtil.isNotEmpty(wxOrderNo)){
            orderUPD.setWxOrderNo(wxOrderNo);
        }
        if(null != callbackPrice){
            orderUPD.setCallbackPrice(callbackPrice);
        }
        if(0 == orderMapper.updateByPrimaryKeySelective(orderUPD)){
            return false;
        }
        return true;
    }

    @Override
    public POrder findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    @Override
    public BigDecimal sumRealPrice(Long uid,Long id){
        return orderMapper.sumRealPrice(uid,id);
    }

    @Override
    public Boolean updateOrder(POrder order){
        if(0 == orderMapper.updateByPrimaryKeySelective(order)){
            return false;
        }
        return true;
    }

    @Override
    public int sumSuitOrderByUid(Long uid,Long id){
        return orderMapper.sumSuitOrderNum(uid,id);
    }

    @Override
    public List<POrder> findUserMonthResultsByPayTime(Long strawUid,Date startTime,Date endTime){
        return orderMapper.findOrderRealPriceByStrawUid(strawUid,startTime,endTime);
    }

    @Override
    public Future<Boolean> overdueOrder(){
        Calendar takeCa = Calendar.getInstance();
        takeCa.add(Calendar.DATE, -15);
        Date takeTime = takeCa.getTime();
        Calendar payCa = Calendar.getInstance();
        payCa.add(Calendar.MINUTE, -30);
        Date payTime = payCa.getTime();
        orderMapper.updateOrderNotTake(takeTime);
        orderMapper.updateOrderNotPay(payTime);
        return new AsyncResult<Boolean>(true);
    }

    private String createOrderNo(Long uid) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(uid).append(System.currentTimeMillis());
        return stringBuffer.toString();
    }
}
