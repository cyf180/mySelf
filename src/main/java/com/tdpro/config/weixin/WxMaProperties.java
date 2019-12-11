package com.tdpro.config.weixin;

import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "wechat.mainapp")
@Data
public class WxMaProperties extends WxMaInMemoryConfig {
    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 设置微信小程序的token
     */
    private String token;

    /**
     * 设置微信小程序的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    }
