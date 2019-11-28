package com.tdpro.service.impl;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.common.utils.weixin.WxPayUtility;
import com.tdpro.common.utils.weixin.entity.WxPayET;
import com.tdpro.entity.POrder;
import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PPayConfig;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.OrderPayETD;
import com.tdpro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
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
    private WxPayUtility wxPayUtility = new WxPayUtility();
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response unifyPay(OrderPayETD payETD) {
        if(null == payETD.getOrderId() || payETD.getOrderId().equals(new Long(0))){
            return ResponseUtils.errorRes("操作异常");
        }
        POrder orderInfo = orderService.findOrderById(payETD.getOrderId());
        if(null == orderInfo){
            return ResponseUtils.errorRes("订单异常");
        }
        if(!orderInfo.getState().equals(new Integer(0))){
            return ResponseUtils.errorRes("该订单不属于待支付");
        }
        if(!orderInfo.getUid().equals(payETD.getUid())){
            return ResponseUtils.errorRes("订单异常");
        }
        Response response = ResponseUtils.errorRes("异常");
        PUser userInfo = userService.findById(payETD.getUid());
        List<POrderVoucher> orderVoucherList = orderVoucherService.findListByOrderId(orderInfo.getId());
        switch (orderInfo.getZoneType().intValue()){
            case 2:
                if(null != orderVoucherList && orderVoucherList.size() >0){
                    userVoucherService.updateUserVoucherIsUse(orderVoucherList);
                    if(!orderService.updateOrderIsPay(orderInfo.getId(),PayType.EXCHANGE_PAY) || !goodsService.updateRepertory(orderInfo)){
                        throw new RuntimeException("订单修改失败");
                    }
                    response = ResponseUtils.successRes(orderInfo.getId());
                }else{
                    response =  ResponseUtils.errorRes("兑换券异常");
                }
                break;
            default:
                PayType payType = PayType.BALANCE_PAY;
                if(payType.getType().equals(payETD.getPayType())){
                    Response updateUser = userService.userBalancePay(orderInfo,userInfo);
                    if(null != updateUser) return updateUser;
                    if(null != orderVoucherList && orderVoucherList.size() >0){
                        userVoucherService.updateUserVoucherIsUse(orderVoucherList);
                    }
                    if(!orderService.updateOrderIsPay(orderInfo.getId(),payType) || !goodsService.updateRepertory(orderInfo)){
                        throw new RuntimeException("订单修改失败");
                    }
                    response = ResponseUtils.successRes(orderInfo.getId());
                }else if(PayType.WX_PAY.getType().equals(payETD.getPayType())){
                    PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"),new Byte("1"));
                    if(null == payConfig){
                        return ResponseUtils.errorRes("微信支付为开启");
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
                    Map msgBean = wxPayUtility.weiXinPayCommon(wxPayET);
                    if (StringUtils.isEmpty(msgBean)||StringUtils.isEmpty(msgBean.get("code"))||"0".equals(msgBean.get("code").toString())){
                        return ResponseUtils.errorRes(msgBean.get("msg").toString());
                    }
                    response = ResponseUtils.successRes(msgBean.get("msg"));
                }
                break;
        }
        return response;
    }
}
