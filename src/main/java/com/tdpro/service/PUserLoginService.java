package com.tdpro.service;

import com.tdpro.common.utils.Response;

public interface PUserLoginService {
    /**
     * 根据用户id查询微信登录信息
     * @param uid
     * @return
     */
    Response findUserLogin(Long uid);
}
