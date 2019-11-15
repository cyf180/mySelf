package com.tdpro.config.jpush;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @About
 * @Author jy
 * @Date 2018/7/12 14:14
 */
@Configuration
@EnableConfigurationProperties({JpushConfig.class})
public class JpushConfiguration {
}
