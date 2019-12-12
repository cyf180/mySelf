package com.tdpro.service.impl;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.PayReturn;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.common.utils.weixin.WxUnifiedOrderUtils;
import com.tdpro.common.utils.weixin.entity.WxPayET;
import com.tdpro.entity.*;
import com.tdpro.entity.extend.OrderPayETD;
import com.tdpro.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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
    private final WxUnifiedOrderUtils wxUnifiedOrderUtils = new WxUnifiedOrderUtils();


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
                    PUserLogin userLogin = userLoginService.findByUid(userInfo.getId());
                    if(null == userLogin){
                        payReturn.setMsg("微信账号异常");
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
                    wxPayET.setAttach("orderPay");
                    wxPayET.setOpenId(userLogin.getOpenId());
                    Map msgBean = wxUnifiedOrderUtils.weiXinPayCommon(wxPayET);
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

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response userPay(PUser user) {
        PUser userInfo = userService.findById(user.getId());
        if(null == userInfo){
            return ResponseUtils.errorRes("用户不存在");
        }
        PUserLogin userLogin = userLoginService.findByUid(userInfo.getId());
        if(null == userLogin){
            return ResponseUtils.errorRes("微信账号异常");
        }
        if(!new Integer(0).equals(userInfo.getIsUser())){
            return  ResponseUtils.errorRes("您已经是会员");
        }
        PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"), new Byte("1"));
        if (null == payConfig) {
            return  ResponseUtils.errorRes("微信支付为开启");
        }
        PUserPayConfig userPayConfig = userPayConfigService.findByType(0);
        if(null == userPayConfig || new BigDecimal("0").compareTo(userPayConfig.getPrice()) >= 0){
            return  ResponseUtils.errorRes("该功能未开启");
        }
        BigDecimal payPrice = userPayConfig.getPrice();
        PUserPay userPay = userPayService.insertUserPayLog(userInfo.getId(),payPrice);
        if(null == userPay){
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
