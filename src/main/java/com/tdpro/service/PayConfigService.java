package com.tdpro.service;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.tdpro.entity.PPayConfig;

public interface PayConfigService {
    /**
     * 支付配置查询
     * @param channel
     * @return
     */
    PPayConfig findByChannelAndPayType(Byte channel,Byte payType);

    WxPayConfig getWxPayConfig(int type);
}
