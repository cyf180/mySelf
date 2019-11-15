package com.tdpro.config.weixin;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @About
 * @Author jy
 * @Date 2018/5/7 13:40
 */
@Configuration
@ConditionalOnClass(WxMaService.class)
@EnableConfigurationProperties({WxMaProperties.class,WxMaTemplateMessageConfig.class,WxMpTemplateMessageConfig.class})
public class WxMaPropertiesConfiguration {

    @Autowired
    private WxMaProperties properties;
    @Bean
    @ConditionalOnMissingBean
    public WxMaConfig configStorage() {
        WxMaInMemoryConfig configStorage = new WxMaInMemoryConfig();
        configStorage.setAppid(this.properties.getAppid());
        configStorage.setSecret(this.properties.getSecret());
        configStorage.setToken(this.properties.getToken());
        configStorage.setAesKey(this.properties.getAesKey());
        return configStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMpService(WxMaConfig configStorage) {
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.okhttp.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.jodd.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.apache.WxMpServiceImpl();
        WxMaService wxMpService = new WxMaServiceImpl();
        wxMpService.setWxMaConfig(configStorage);
        return wxMpService;
    }

}
