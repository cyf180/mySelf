package com.tdpro.common.utils.weixin;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import com.tdpro.common.utils.weixin.entity.WxPayET;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.tdpro.common.utils.AmountUtils.CURRENCY_FEN_REGEX;
@Slf4j
public class WxPayUtility {
    private Lock lock = new ReentrantLock();
    private WxPayService wxPayService=new WxPayServiceImpl();
    public Map<String,Object> weiXinPayCommon(WxPayET wxPayET) {
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
