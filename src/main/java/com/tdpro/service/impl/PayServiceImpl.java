package com.tdpro.service.impl;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.pagehelper.StringUtil;
import com.sun.org.apache.regexp.internal.RE;
import com.tdpro.common.constant.PayType;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.PayReturn;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.common.utils.weixin.WxUnifiedOrderUtils;
import com.tdpro.common.utils.weixin.entity.WxPayET;
import com.tdpro.entity.*;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.entity.extend.GoodsExchangeETD;
import com.tdpro.entity.extend.OrderPayETD;
import com.tdpro.service.*;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PayServiceImpl implements PayService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderVoucherService orderVoucherService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserVoucherService userVoucherService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PayConfigService payConfigService;
    @Autowired
    private UserPayConfigService userPayConfigService;
    @Autowired
    private UserPayService userPayService;
    @Autowired
    private PUserLoginService userLoginService;
    @Autowired
    private GoodsExchangeService exchangeService;
    @Autowired
    private UserSiteService userSiteService;
    private final WxUnifiedOrderUtils wxUnifiedOrderUtils = new WxUnifiedOrderUtils();


    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public PayReturn unifyPay(OrderPayETD payETD) {
        PayReturn payReturn = new PayReturn();
        if (null == payETD.getOrderId() || payETD.getOrderId().equals(new Long(0))) {
            payReturn.setMsg("操作异常");
            return payReturn;
        }
        if(null == payETD.getSiteId()){
            payReturn.setMsg("请选择收货地址");
            return payReturn;
        }
        PUserSite site = userSiteService.findById(payETD.getSiteId());
        if(null == site || !site.getUid().equals(payETD.getUid())){
            payReturn.setMsg("收货地址异常");
            return payReturn;
        }
        POrder orderInfo = orderService.findOrderById(payETD.getOrderId());
        if (null == orderInfo) {
            payReturn.setMsg("订单异常");
            return payReturn;
        }
        if (!orderInfo.getState().equals(new Integer(0))) {
            payReturn.setMsg("该订单不属于待支付");
            return payReturn;
        }
        if (!orderInfo.getUid().equals(payETD.getUid())) {
            payReturn.setMsg("订单异常");
            return payReturn;
        }
        String payPwd;
        Long orderId = orderInfo.getId();
        GoodsETD goodsInfo = goodsService.selectInfo(orderInfo.getGoodsId());
        PUser userInfo = userService.findById(payETD.getUid());
        switch (orderInfo.getZoneType().intValue()) {
            case 2:
                if(StringUtil.isEmpty(userInfo.getPayPassword())){
                    payReturn.setMsg("未设置支付密码");
                    return payReturn;
                }
                if(StringUtil.isEmpty(payETD.getPayPassword())){
                    payReturn.setMsg("请输入支付密码");
                    return payReturn;
                }
                payPwd = DigestUtil.md5Hex(payETD.getPayPassword());
                if(!payPwd.equals(userInfo.getPayPassword())){
                    payReturn.setMsg("支付密码错误");
                    return payReturn;
                }
                payReturn = this.orderExchangePay(orderInfo);
                break;
            case 1:
                if(StringUtil.isEmpty(userInfo.getPayPassword())){
                    payReturn.setMsg("未设置支付密码");
                    return payReturn;
                }
                if(StringUtil.isEmpty(payETD.getPayPassword())){
                    payReturn.setMsg("请输入支付密码");
                    return payReturn;
                }
                payPwd = DigestUtil.md5Hex(payETD.getPayPassword());
                if(!payPwd.equals(userInfo.getPayPassword())){
                    payReturn.setMsg("支付密码错误");
                    return payReturn;
                }
                payReturn = this.orderMemberPay(orderInfo);
                break;
            case 0:
                payReturn = this.orderOrdinaryPay(orderInfo,payETD,goodsInfo,userInfo);
                break;
        }
        if(payReturn.getState()) {
            POrder orderUPD = new POrder();
            orderUPD.setId(orderId);
            if (StringUtil.isNotEmpty(payETD.getUserNote())) {
                orderUPD.setUserNote(payETD.getUserNote());
            }
            orderUPD.setReceiptName(site.getName());
            orderUPD.setReceiptPhone(site.getPhone());
            orderUPD.setReceiptSite(site.getSite());
            if (!orderService.updateOrder(orderUPD)) {
                throw new BusinessException("订单收货地址更新失败");
            }
        }
        return payReturn;
    }

    private PayReturn orderOrdinaryPay(POrder orderInfo,OrderPayETD payETD,GoodsETD goodsInfo,PUser userInfo){
        PayReturn payReturn = new PayReturn();
        BigDecimal realPrice = new BigDecimal("0");
        BigDecimal totalPrice = orderInfo.getTotalPrice();
        List<PUserVoucher> voucherList = null;
        long uid = userInfo.getId();
        if (null != payETD.getVoucherId() && !payETD.getVoucherId().equals(new Long(0))) {
            GoodsExchangeETD goodsVoucher = exchangeService.selectByGoodsIdAndVoucherId(goodsInfo.getId(), payETD.getVoucherId());
            if (null == goodsVoucher) {
                payReturn.setMsg("该商品不能使用当前券");
                return payReturn;
            }
            if (goodsVoucher.getNumber().compareTo(new Integer(0)) > 0) {
                voucherList = userVoucherService.selectByUidAndVoucherId(uid, goodsVoucher.getVoucherId(), goodsVoucher.getNumber());
                if (null == voucherList || voucherList.size() < goodsVoucher.getNumber().intValue()) {
                    payReturn.setMsg("您的" + goodsVoucher.getVoucherName() + "数量不足");
                    return payReturn;
                }
                BigDecimal needNum = new BigDecimal(goodsVoucher.getNumber().toString());
                BigDecimal faceValue = goodsVoucher.getFaceValue();
                BigDecimal discountAmount = faceValue.multiply(needNum).setScale(2, BigDecimal.ROUND_DOWN);
                realPrice = totalPrice.subtract(discountAmount);
                if (realPrice.compareTo(new BigDecimal(0)) < 0) {
                    realPrice = new BigDecimal("0");
                }
            }
        }else{
            realPrice = totalPrice;
        }
        if (PayType.BALANCE_PAY.getType().equals(payETD.getPayType())) {
            if(StringUtil.isEmpty(userInfo.getPayPassword())){
                payReturn.setMsg("未设置支付密码");
                return payReturn;
            }
            if(StringUtil.isEmpty(payETD.getPayPassword())){
                payReturn.setMsg("请输入支付密码");
                return payReturn;
            }
            String payPwd = DigestUtil.md5Hex(payETD.getPayPassword());
            if(!payPwd.equals(userInfo.getPayPassword())){
                payReturn.setMsg("支付密码错误");
                return payReturn;
            }
            PayType payType = PayType.BALANCE_PAY;
            Response updateUser = userService.userBalancePay(orderInfo, userInfo,realPrice);
            if (null != updateUser) {
                payReturn.setMsg(updateUser.getMsg());
                return payReturn;
            }
            if (null != voucherList && voucherList.size() > 0) {
                if (!userVoucherService.updateUserVoucherIsUse(voucherList)) {
                    throw new RuntimeException("使用券失败");
                }
            }
            if (!orderService.updateOrderIsPay(orderInfo.getId(), payType, null, null, realPrice) || !goodsService.updateRepertory(orderInfo)) {
                throw new RuntimeException("订单修改失败");
            }
            payReturn.setState(true);
            payReturn.setType(1);
            payReturn.setOrderId(orderInfo.getId());
        } else if (PayType.WX_PAY.getType().equals(payETD.getPayType()) && realPrice.compareTo(new BigDecimal("0")) > 0) {
            PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"), new Byte("1"));
            if (null == payConfig) {
                payReturn.setMsg("微信支付为开启");
                return payReturn;
            }
            PUserLogin userLogin = userLoginService.findByUid(userInfo.getId());
            if (null == userLogin) {
                payReturn.setMsg("微信账号异常");
                return payReturn;
            }
            if (null != voucherList && voucherList.size() > 0) {
                if (!userVoucherService.updateUserVoucherIsLock(voucherList)) {
                    throw new RuntimeException("优惠券绑定失败");
                }
            }
            POrder orderSET = new POrder();
            orderSET.setId(orderInfo.getId());
            orderSET.setRealPrice(realPrice);
            if(!orderService.updateOrder(orderSET)){
                throw new RuntimeException("订单实付价格更新失败");
            }
            WxPayET wxPayET = new WxPayET();
            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(payConfig.getMchId());
            wxPayConfig.setMchKey(payConfig.getPaySecret());
            wxPayConfig.setAppId(payConfig.getAppId());
            wxPayConfig.setKeyPath(payConfig.getCertPath());
            wxPayConfig.setNotifyUrl(payConfig.getBackPath());
            wxPayET.setWxPayConfig(wxPayConfig);
            wxPayET.setTradeType(WxPayConstants.TradeType.JSAPI);
            wxPayET.setCreateIp("127.0.0.1");
            wxPayET.setOutTradeNo(orderInfo.getOrderNo());
            wxPayET.setTotalFee(realPrice);
            wxPayET.setAttach("orderPay");
            wxPayET.setOpenId(userLogin.getOpenId());
            Map msgBean = wxUnifiedOrderUtils.weiXinPayCommon(wxPayET);
            if (StringUtils.isEmpty(msgBean) || StringUtils.isEmpty(msgBean.get("code")) || "0".equals(msgBean.get("code").toString())) {
                throw new BusinessException(msgBean.get("msg").toString());
            }
            payReturn.setState(true);
            payReturn.setType(2);
            payReturn.setSuccessRes(msgBean.get("msg"));
        }else{
            payReturn.setMsg("支付异常");
        }
        return payReturn;
    }

    private PayReturn orderMemberPay(POrder orderInfo){
        PayReturn payReturn = new PayReturn();
        if(!orderInfo.getNumber().equals(new Integer(1))){
            payReturn.setMsg("会员产品只能免费领取一件");
            return payReturn;
        }
        if (!orderService.updateOrderIsPay(orderInfo.getId(), PayType.MEMBER_PAY, null, null, new BigDecimal(0)) || !goodsService.updateRepertory(orderInfo)) {
            throw new RuntimeException("订单修改失败");
        }
        payReturn.setState(true);
        payReturn.setType(0);
        payReturn.setOrderId(orderInfo.getId());
        return payReturn;
    }
    private PayReturn orderExchangePay(POrder orderInfo){
        long orderId = orderInfo.getId();
        long uid = orderInfo.getUid();
        int orderNumber = orderInfo.getNumber();
        PayReturn payReturn = new PayReturn();
        List<GoodsExchangeETD> goodsExchangeList = exchangeService.selectListByGoodsId(orderInfo.getGoodsId());
        if (null == goodsExchangeList || goodsExchangeList.size() <= 0) {
            payReturn.setMsg("商品配置异常");
            return payReturn;
        }
        for (GoodsExchangeETD exchange : goodsExchangeList) {
            if (exchange.getNumber().compareTo(new Integer(0)) <= 0) {
                payReturn.setMsg("商品兑换配置异常");
                return payReturn;
            }
            Long voucherId = exchange.getVoucherId();
            int needNum = exchange.getNumber() * orderNumber;
            List<PUserVoucher> voucherList = userVoucherService.selectByUidAndVoucherId(orderInfo.getUid(), voucherId, needNum);
            if (null == voucherList || voucherList.size() < needNum) {
                payReturn.setMsg("您的" + exchange.getVoucherName() + "数量不足");
                return payReturn;
            }
            for (PUserVoucher userVoucher : voucherList) {
                if (!orderVoucherService.insertVoucher(orderId, uid, userVoucher.getId())) {
                    throw new RuntimeException("券绑定失败");
                }
            }
            if (!userVoucherService.updateUserVoucherIsUse(voucherList)) {
                throw new RuntimeException("使用绑定失败");
            }
        }
        boolean orderIsPay = orderService.updateOrderIsPay(orderInfo.getId(), PayType.EXCHANGE_PAY, null, null, new BigDecimal("0"));
        boolean goodsUpd = goodsService.updateRepertory(orderInfo);
        if (!orderIsPay || !goodsUpd) {
            throw new RuntimeException("订单修改失败");
        }
        payReturn.setState(true);
        payReturn.setType(0);
        payReturn.setOrderId(orderInfo.getId());
        return payReturn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response userPay(PUser user) {
        PUser userInfo = userService.findById(user.getId());
        if (null == userInfo) {
            return ResponseUtils.errorRes("用户不存在");
        }
        PUserLogin userLogin = userLoginService.findByUid(userInfo.getId());
        if (null == userLogin) {
            return ResponseUtils.errorRes("微信账号异常");
        }
        if (!new Integer(0).equals(userInfo.getIsUser())) {
            return ResponseUtils.errorRes("您已经是会员");
        }
        PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"), new Byte("1"));
        if (null == payConfig) {
            return ResponseUtils.errorRes("微信支付为开启");
        }
        PUserPayConfig userPayConfig = userPayConfigService.findByType(0);
        if (null == userPayConfig || new BigDecimal("0").compareTo(userPayConfig.getPrice()) >= 0) {
            return ResponseUtils.errorRes("该功能未开启");
        }
        BigDecimal payPrice = userPayConfig.getPrice();
        PUserPay userPay = userPayService.insertUserPayLog(userInfo.getId(), payPrice);
        if (null == userPay) {
            throw new RuntimeException("添加购买订单失败");
        }
        WxPayET wxPayET = new WxPayET();
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(payConfig.getMchId());
        wxPayConfig.setMchKey(payConfig.getPaySecret());
        wxPayConfig.setAppId(payConfig.getAppId());
        wxPayConfig.setKeyPath(payConfig.getCertPath());
        wxPayConfig.setNotifyUrl(payConfig.getUserBackPath());
        wxPayET.setWxPayConfig(wxPayConfig);
        wxPayET.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayET.setCreateIp("127.0.0.1");
        wxPayET.setOutTradeNo(userPay.getPayNo());
        wxPayET.setTotalFee(payPrice);
        wxPayET.setAttach("userBuy");
        wxPayET.setOpenId(userLogin.getOpenId());
        Map msgBean = wxUnifiedOrderUtils.weiXinPayCommon(wxPayET);
        if (StringUtils.isEmpty(msgBean) || StringUtils.isEmpty(msgBean.get("code")) || "0".equals(msgBean.get("code").toString())) {
            return ResponseUtils.errorRes(msgBean.get("msg").toString());
        }
        return ResponseUtils.successRes(msgBean.get("msg"));
    }
}
