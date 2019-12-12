package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PUserLogin;

public interface PUserLoginService {
    /**
     * 根据用户id查询微信登录信息
     * @param uid
     * @return
     */
    Response findUserLogin(Long uid);

    /**
     * 根据openid查询
     * @param openId
     * @return
     */
    PUserLogin findByOpenId(String openId);

    /**
     * 添加三方登录
     * @param userLogin
     * @return
     */
    int insertUserLog(PUserLogin userLogin);

    /**
     * 根据id查询
     * @return
     */
    PUserLogin findById(Long id);

    /**
     * 修改
     * @param userLogin
     * @return
     */
    Boolean updateUserLogin(PUserLogin userLogin);

    /**
     * 根据uid查询
     * @param uid
     * @return
     */
    PUserLogin findByUid(Long uid);
}
