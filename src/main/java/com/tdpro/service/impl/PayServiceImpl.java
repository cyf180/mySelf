package com.tdpro.service.impl;

import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.POrder;
import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.OrderPayETD;
import com.tdpro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    @Override
    public Response balancePay(OrderPayETD payETD) {
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
                    if(!orderService.updateOrderIsPay(orderInfo.getUid(),PayType.EXCHANGE_PAY)){
                        throw new RuntimeException("订单修改失败");
                    }
                    response = ResponseUtils.successRes(orderInfo.getId());
                }else{
                    response =  ResponseUtils.errorRes("兑换券异常");
                }
            default:
                PayType payType = PayType.BALANCE_PAY;
                if(payType.getType().equals(payETD.getPayType())){
                    if(null != orderVoucherList && orderVoucherList.size() >0){
                        userVoucherService.updateUserVoucherIsUse(orderVoucherList);
                    }
                    Response updateUser = userService.userBalancePay(orderInfo,userInfo);
                    if(null != updateUser) response = updateUser;
                    if(!orderService.updateOrderIsPay(orderInfo.getUid(),payType)){
                        throw new RuntimeException("订单修改失败");
                    }
                    response = ResponseUtils.successRes(orderInfo.getId());
                }else if(PayType.WX_PAY.getType().equals(payETD.getPayType())){
                    response = ResponseUtils.successRes("1");
                }
                break;
        }
        return response;
    }
}
