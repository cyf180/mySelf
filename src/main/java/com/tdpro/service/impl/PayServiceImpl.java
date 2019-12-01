package com.tdpro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.PayReturn;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.weixin.entity.WxPayET;
import com.tdpro.entity.POrder;
import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PPayConfig;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.OrderPayETD;
import com.tdpro.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.tdpro.common.utils.AmountUtils.CURRENCY_FEN_REGEX;

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
    private WxPayService wxPayService=new WxPayServiceImpl();

    private Lock lock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public PayReturn unifyPay(OrderPayETD payETD) {
        PayReturn payReturn = new PayReturn();
        payReturn.setState(false);
        if (null == payETD.getOrderId() || payETD.getOrderId().equals(new Long(0))) {
            payReturn.setMsg("操作异常");
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
        PUser userInfo = userService.findById(payETD.getUid());
        List<POrderVoucher> orderVoucherList = orderVoucherService.findListByOrderId(orderInfo.getId());
        switch (orderInfo.getZoneType().intValue()) {
            case 2:
                if (null != orderVoucherList && orderVoucherList.size() > 0) {
                    if (!userVoucherService.updateUserVoucherIsUse(orderVoucherList)) {
                        throw new RuntimeException("修改优惠券失败");
                    }
                    if (!orderService.updateOrderIsPay(orderInfo.getId(), PayType.EXCHANGE_PAY, null, null) || !goodsService.updateRepertory(orderInfo)) {
                        throw new RuntimeException("订单修改失败");
                    }
                    payReturn.setState(true);
                    payReturn.setOrderId(orderInfo.getId());
                } else {
                    payReturn.setMsg("兑换券异常");
                    return payReturn;
                }
                break;
            default:
                PayType payType = PayType.BALANCE_PAY;
                if (payType.getType().equals(payETD.getPayType())) {
                    Response updateUser = userService.userBalancePay(orderInfo, userInfo);
                    if (null != updateUser) {
                        payReturn.setMsg(updateUser.getMsg());
                        return payReturn;
                    }
                    if (null != orderVoucherList && orderVoucherList.size() > 0) {
                        if (!userVoucherService.updateUserVoucherIsUse(orderVoucherList)) {
                            throw new RuntimeException("修改优惠券失败");
                        }
                    }
                    if (!orderService.updateOrderIsPay(orderInfo.getId(), payType, null, null) || !goodsService.updateRepertory(orderInfo)) {
                        throw new RuntimeException("订单修改失败");
                    }
                    payReturn.setState(true);
                    payReturn.setType(1);
                    payReturn.setOrderId(orderInfo.getId());
                } else if (PayType.WX_PAY.getType().equals(payETD.getPayType())) {
                    PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"), new Byte("1"));
                    if (null == payConfig) {
                        payReturn.setMsg("微信支付为开启");
                        return payReturn;
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
                    wxPayET.setTotalFee(orderInfo.getRealPrice());
                    wxPayET.setAttach("buy");
                    wxPayET.setOpenId("asdada");
                    Map msgBean = this.weiXinPayCommon(wxPayET);
                    if (StringUtils.isEmpty(msgBean) || StringUtils.isEmpty(msgBean.get("code")) || "0".equals(msgBean.get("code").toString())) {
                        payReturn.setMsg(msgBean.get("msg").toString());
                        return payReturn;
                    }
                    payReturn.setState(true);
                    payReturn.setType(2);
                    payReturn.setSuccessRes(msgBean.get("msg"));
                }
                break;
        }
        return payReturn;
    }

    private Map<String,Object> weiXinPayCommon(WxPayET wxPayET) {
        Map<String,Object> msgBean=new HashMap<String,Object>();
        msgBean.put("code","0");
        String attach="";
        String body="购买商品";
        try {
            lock.lock();
            if (StringUtils.isEmpty(wxPayET.getTotalFee()) || new BigDecimal(0).compareTo(wxPayET.getTotalFee())>=0){
                msgBean.put("msg","金额输入不合法");
                return msgBean;
            }
            if (StringUtils.isEmpty(wxPayET.getOpenId())){
                msgBean.put("msg","openId必填");
                return msgBean;
            }
            if (StringUtils.isEmpty(wxPayET.getTradeType())){
                msgBean.put("msg","请填写支付类型");
                return msgBean;
            }
            if (StringUtils.isEmpty(wxPayET.getOutTradeNo())){
                msgBean.put("msg","订单编号错误");
                return msgBean;
            }
            if (StringUtils.isEmpty(wxPayET.getCreateIp())){
                msgBean.put("msg","IP错误");
                return msgBean;
            }
            // 查询用户关联商户信息
            WxPayConfig wxPayConfig=wxPayET.getWxPayConfig();
            if (wxPayConfig==null){
                msgBean.put("msg","商户支付信息错误");
                return msgBean;
            }
            if (StringUtils.isEmpty(wxPayET.getWxPayConfig().getNotifyUrl())){
                msgBean.put("msg","回调地址错误");
                return msgBean;
            }
            BigDecimal orderPrice=wxPayET.getTotalFee();
            //金额元转为分
            Integer totalFee= BaseWxPayRequest.yuanToFen(orderPrice.toString());
//            totalFee=1;
            if(!totalFee.toString().matches(CURRENCY_FEN_REGEX)) {
                msgBean.put("msg","金额格式有误");
                return msgBean;
            }
            WxPayUnifiedOrderRequest data=new WxPayUnifiedOrderRequest();
            data.setOutTradeNo(wxPayET.getOutTradeNo());
            data.setBody(body);
            data.setFeeType("CNY");
            data.setTradeType(wxPayET.getTradeType());
            data.setDeviceInfo("");
            data.setTotalFee(totalFee);
            data.setSpbillCreateIp(wxPayET.getCreateIp());
            data.setNotifyUrl(wxPayET.getWxPayConfig().getNotifyUrl());
            data.setAttach(attach);
            data.setOpenid(wxPayET.getOpenId());
            log.info("微信统一下单,参数{}",JSONObject.toJSONString(data));
            wxPayService.setConfig(wxPayConfig);
            WxPayUnifiedOrderResult resultData=wxPayService.unifiedOrder(data);
            String str= JSONObject.toJSONString(resultData);
            log.info("微信统一下单结果: {}",str);
            if ("SUCCESS".equals(resultData.getResultCode())){
                String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
                Map<String, String> payInfo = new HashMap();
                payInfo.put("appId", resultData.getAppid());
                payInfo.put("timeStamp", timestamp);
                payInfo.put("nonceStr", resultData.getNonceStr());
                payInfo.put("package", "prepay_id=" + resultData.getPrepayId());
                payInfo.put("signType", "MD5");
                payInfo.put("sign", SignUtils.createSign(payInfo,(String)null, wxPayConfig.getMchKey(), false));
                msgBean.put("code",1);
                msgBean.put("msg", JSONObject.toJSONString(payInfo));
            }else {
                msgBean.put("msg",resultData.getReturnMsg());
            }
            return msgBean;
        }catch (Exception e){
            log.error("统一下单失败: {}",e.getMessage());
            msgBean.put("msg","下单失败");
            return msgBean;
        }finally {
            lock.unlock();
        }
    }
}
