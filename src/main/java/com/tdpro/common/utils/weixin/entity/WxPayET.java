package com.tdpro.common.utils.weixin.entity;

import com.github.binarywang.wxpay.config.WxPayConfig;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WxPayET {
    private String openId;
    private BigDecimal totalFee;
    private String createIp;
    private String attach;
    private String outTradeNo;
    private String tradeType;
    private WxPayConfig wxPayConfig;
}
