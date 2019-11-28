package com.tdpro.service.impl;

import com.tdpro.entity.PPayConfig;
import com.tdpro.mapper.PPayConfigMapper;
import com.tdpro.service.PayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayConfigServiceImpl implements PayConfigService {
    @Autowired
    private PPayConfigMapper payConfigMapper;
    @Override
    public PPayConfig findByChannelAndPayType(Byte channel, Byte payType) {
        return payConfigMapper.findByChannelAndPayType(channel,payType);
    }
}
