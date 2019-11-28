package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.OrderPayETD;
import com.tdpro.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/pay/")
@Api(tags = "用户端 - 支付相关")
public class PayController {
    @Autowired
    private PayService payService;

    @PostMapping("unifyPay")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "payType", value = "支付类型(0:余额 1:微信)", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "统一支付")
    public Response getPay(@ApiIgnore @RequestAttribute Long uid, @Valid @RequestBody OrderPayETD orderPayETD){
        orderPayETD.setUid(uid);
        return payService.unifyPay(orderPayETD);
    }
}
