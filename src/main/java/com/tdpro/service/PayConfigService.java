package com.tdpro.service;

import com.tdpro.entity.PPayConfig;

public interface PayConfigService {
    /**
     * 支付配置查询
     * @param channel
     * @return
     */
    PPayConfig findByChannelAndPayType(Byte channel,Byte payType);
}
