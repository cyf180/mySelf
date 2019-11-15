package com.tdpro.config.jpush;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @About 极光推送配置文件
 * @Author jy
 * @Date 2018/7/12 14:13
 */
@ConfigurationProperties(prefix = "jPush.config")
@Data
public class JpushConfig {
    String appKey;
    String secret;
}
