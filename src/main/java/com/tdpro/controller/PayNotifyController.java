package com.tdpro.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.tdpro.service.PayNotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RestController
@RequestMapping("/td/notify/")
@Api(tags = "用户端 - 回调相关")
@Slf4j
public class PayNotifyController {
    @Autowired
    private PayNotifyService payNotifyService;
    @Autowired
    private WxPayService wxPayService;
    @PostMapping("wxOrderPayNotify")
    @ApiOperation(value = "微信支付回调")
    public void wxOrderPayNotify(HttpServletRequest request, HttpServletResponse response){
        log.info("微信订单支付回调开始");
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            log.info("回调订单号xmlResult: {}",xmlResult);
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            log.info("回调结果result: {}",result);

            //处理结果
            boolean notify = payNotifyService.orderPayNotify(result);
            PrintWriter printWriter = response.getWriter();
            returnMsg(result.getOutTradeNo(), notify, printWriter);
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信订单回调出错{}",e);
        }
    }

    private void returnMsg(String payNo, Boolean notify, PrintWriter printWriter) {
        if(notify) {
            log.info("微信回调处理结果成功");
            printWriter.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[ok]]></return_msg></xml>");
        } else {
            log.error("微信回调处理结果出错，本地单号：{}",payNo);
            printWriter.write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[result_code is FAIL]]></return_msg></xml>");
        }
    }
}
