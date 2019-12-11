package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.LoginETD;


public interface LoginService {

    /**
     *  管理员登录
     * @param loginETD
     * @return
     */
    Response adminLogin(LoginETD loginETD);
}
