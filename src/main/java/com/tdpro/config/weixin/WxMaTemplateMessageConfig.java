package com.tdpro.config.weixin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @About
 * @Author jy
 * @Date 2018/7/5 13:43
 */
@ConfigurationProperties(prefix = "wechat.template")
@Data
public class WxMaTemplateMessageConfig {
    String storageTemplateId;
    String confirmTemplateId;
    String takeTemplateId;
    String errorTemplateId;
    String couponReceive;
    String couponExpire;
    String cashBackTemplateId;
}
