package com.tdpro.common.utils.jwt;

import lombok.Data;

/**
 * @About
 * @Author jy
 * @Date 2018/6/6 13:55
 */
@Data
public class JwtSettings {
    /**
     * 有效时间
     */
    private Integer tokenExpirationTime;

    private String tokenIssuer;

    private String tokenSigningKey;

    private Integer refreshTokenExpTime;
}
