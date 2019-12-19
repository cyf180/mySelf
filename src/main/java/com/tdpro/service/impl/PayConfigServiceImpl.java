package com.tdpro.service.impl;

import com.github.binarywang.wxpay.config.WxPayConfig;
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

    public WxPayConfig getWxPayConfig(int type){
        PPayConfig payConfig = this.findByChannelAndPayType(new Byte("0"), new Byte("1"));
        if(null == payConfig){
            return null;
        }
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(payConfig.getMchId());
        wxPayConfig.setMchKey(payConfig.getPaySecret());
        wxPayConfig.setAppId(payConfig.getAppId());
        wxPayConfig.setKeyPath(payConfig.getCertPath());
        if(type == 0) {
            wxPayConfig.setNotifyUrl(payConfig.getBackPath());
        }else if(type == 1){
            wxPayConfig.setNotifyUrl(payConfig.getUserBackPath());
        }
        return wxPayConfig;
    }
}
