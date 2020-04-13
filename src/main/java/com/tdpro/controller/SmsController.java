package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.SmsSendETD;
import com.tdpro.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/sms/")
@Api(tags = "用户端 - 短信相关")
public class SmsController {
    @Autowired
    private SmsService smsService;

    @PostMapping("enrollSend")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "string", paramType = "form"),
    })
    @ApiOperation(value = "注册验证码发送")
    public Response userEnroll(@Valid @RequestBody SmsSendETD sendETD){
        return smsService.enrollSmsSend(sendETD);
    }

//    @GetMapping("send")
//    public Response send(){
//        return smsService.sendSms();
//    }
}
