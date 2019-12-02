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
import com.tdpro.common.utils.weixin.WxPayUtils;
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
    private final WxPayUtils wxPayUtils = new WxPayUtils();


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
                    Map msgBean = wxPayUtils.weiXinPayCommon(wxPayET);
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
}
