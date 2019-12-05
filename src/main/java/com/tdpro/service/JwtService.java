package com.tdpro.service;

import com.tdpro.common.OnlineUserInfo;

public interface JwtService {
    String createToken(OnlineUserInfo onlineUserInfo);
}
