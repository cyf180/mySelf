package com.tdpro.service;

import com.tdpro.common.utils.Response;

public interface UserService {
    /**
     * 会员中心
     * @param uid
     * @return
     */
    Response userInformation(Long uid);
}
