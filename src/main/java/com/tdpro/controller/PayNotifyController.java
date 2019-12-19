package com.tdpro.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.tdpro.service.KnotService;
import com.tdpro.service.PayConfigService;
import com.tdpro.service.PayNotifyService;
import com.tdpro.service.UserPayService;
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
import java.util.concurrent.Future;

@RestController
@RequestMapping("/td/notify/")
@Api(tags = "回调相关")
@Slf4j
public class PayNotifyController {
    @Autowired
    private PayNotifyService payNotifyService;
    @Autowired
    private KnotService knotService;
    @Autowired
    private PayConfigService payConfigService;
    private WxPayService wxPayService= new WxPayServiceImpl();
    @PostMapping("wxOrderPayNotify")
    @ApiOperation(value = "微信支付回调")
    public void wxOrderPayNotify(HttpServletRequest request, HttpServletResponse response){
        log.info("微信订单支付回调开始");
        try {
            WxPayConfig wxPayConfig = payConfigService.getWxPayConfig(0);
            wxPayService.setConfig(wxPayConfig);
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            log.info("微信订单支付回调订单号xmlResult: {}",xmlResult);
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            log.info("微信订单支付回调结果result: {}",result);

            //处理结果
            Long orderId = payNotifyService.orderPayNotify(result);
            PrintWriter printWriter = response.getWriter();
            if(!new Long(0).equals(orderId)) {
                Future<Boolean> knot =  knotService.knot(orderId);
                log.info("微信回调处理结果成功");
                printWriter.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[ok]]></return_msg></xml>");
            } else {
                log.error("微信回调处理结果出错，本地单号：{}",result.getOutTradeNo());
                printWriter.write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[result_code is FAIL]]></return_msg></xml>");
            }
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信订单支付回调出错{}",e);
        }
    }

    @PostMapping("buyMemberNotify")
    @ApiOperation(value = "购买会员微信支付回调")
    public void buyMemberNotify(HttpServletRequest request, HttpServletResponse response){
        log.info("微信会员购买支付回调开始");
        try {
            WxPayConfig wxPayConfig = payConfigService.getWxPayConfig(1);
            wxPayService.setConfig(wxPayConfig);
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            log.info("微信会员购买回调订单号xmlResult: {}",xmlResult);
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            log.info("微信会员购买回调结果result: {}",result);
            //处理结果
            boolean notify = payNotifyService.userByNotify(result);
            PrintWriter printWriter = response.getWriter();
            returnMsg(result.getOutTradeNo(), notify, printWriter);
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信会员购买回调出错{}",e);
        }
    }

    private void returnMsg(String payNo, boolean notify, PrintWriter printWriter) {
        if(notify) {
            log.info("微信回调处理结果成功");
            printWriter.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[ok]]></return_msg></xml>");
        } else {
            log.error("微信回调处理结果出错，本地单号：{}",payNo);
            printWriter.write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[result_code is FAIL]]></return_msg></xml>");
        }
    }
}
