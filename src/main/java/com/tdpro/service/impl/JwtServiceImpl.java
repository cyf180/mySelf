package com.tdpro.service.impl;

import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.utils.jwt.JwtToken;
import com.tdpro.common.utils.jwt.JwtTokenFactory;
import com.tdpro.config.jwt.JwtConfig;
import com.tdpro.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class JwtServiceImpl implements JwtService {
    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public String createToken(OnlineUserInfo onlineUserInfo) {
        JwtTokenFactory tokenFactory=new JwtTokenFactory();
        tokenFactory.setSettings(jwtConfig);
        JwtToken accessToken = tokenFactory.createAccessJwtToken(onlineUserInfo);
        if(accessToken!=null&& StringUtils.hasText(accessToken.getToken())){
            return accessToken.getToken();
        }
        return null;
    }
}
