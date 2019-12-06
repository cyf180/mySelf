package com.tdpro.config.jwt;


import com.tdpro.common.utils.jwt.JwtSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @About
 * @Author jy
 * @Date 2018/6/6 13:55
 */
@Configuration
@ConfigurationProperties(prefix = "com.tdpro.common.utils.jwt")
@Data
public class JwtConfig extends JwtSettings {

}
