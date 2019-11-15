package com.tdpro.common.utils.jwt;

import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.utils.ToolUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

/**
 * @About
 * @Author jy
 * @Date 2018/6/6 16:17
 */

public class JwtTokenFactory {
    private JwtSettings settings;


    public void setSettings(JwtSettings settings) {
        this.settings = settings;
    }

    public JwtSettings getSettings() {
        return settings;
    }

    public AccessJwtToken createAccessJwtToken(OnlineUserInfo onlineUserInfo) {
        if (null==onlineUserInfo.getId()) {
            throw new IllegalArgumentException("Cannot create JWT Token without userId");
        }
        String key= ToolUtil.getRedisKey(onlineUserInfo.getId().toString());
        Claims claims = Jwts.claims().setSubject(onlineUserInfo.getLoginRole());
        DateTime currentTime = new DateTime();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setId(key)
                .setIssuedAt(currentTime.toDate())
                .setExpiration(currentTime.plusMinutes(settings.getTokenExpirationTime()).toDate())
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(OnlineUserInfo onlineUserInfo) {
        if (null==onlineUserInfo.getId()) {
            throw new IllegalArgumentException("Cannot create JWT Token without userId");
        }

        DateTime currentTime = new DateTime();
        String key= ToolUtil.getRedisKey(onlineUserInfo.getId().toString());
        Claims claims = Jwts.claims().setSubject(onlineUserInfo.getLoginRole());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setId(key)
                .setIssuedAt(currentTime.toDate())
                .setExpiration(currentTime.plusMinutes(settings.getRefreshTokenExpTime()).toDate())
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }
}
