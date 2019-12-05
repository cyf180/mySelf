package com.tdpro.controller;

import com.tdpro.common.utils.PayReturn;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.extend.OrderPayETD;
import com.tdpro.service.KnotService;
import com.tdpro.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/user/pay/")
@Api(tags = "用户端 - 支付相关")
public class PayController {
    @Autowired
    private PayService payService;
    @Autowired
    private KnotService knotService;
    private Lock payLok = new ReentrantLock();
    @PostMapping("unifyPay")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "payType", value = "支付类型(0:余额 1:微信)", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "统一支付")
    public Response getPay(@ApiIgnore @RequestAttribute Long uid, @Valid @RequestBody OrderPayETD orderPayETD){
        Response response;
        try {
            payLok.lock();
            orderPayETD.setUid(uid);
            PayReturn payReturn = payService.unifyPay(orderPayETD);
            if(payReturn.getState()){
                if(payReturn.getType().equals(new Integer(1))){
                    Future<Boolean> knot =  knotService.knot(payReturn.getOrderId());
                    response = ResponseUtils.successRes(payReturn.getOrderId());
                }else if(payReturn.getType().equals(new Integer(2))){
                    response = ResponseUtils.successRes(payReturn.getSuccessRes());
                }else{
                    response = ResponseUtils.successRes(payReturn.getOrderId());
                }
            }else {
                response = ResponseUtils.errorRes(payReturn.getMsg());
            }
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            payLok.unlock();
        }
        return response;
    }
}
