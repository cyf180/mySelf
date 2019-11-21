package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserSiteETD;

public interface UserSiteService {
    /**
     * 用户收货地址列表
     * @param siteETD
     * @return
     */
    Response userSiteList(UserSiteETD siteETD);

    /**
     * 用户收货地址删除
     * @param siteETD
     * @return
     */
    Response delUserSite(UserSiteETD siteETD);

    /**
     * 设置默认收货地址
     * @param siteETD
     * @return
     */
    Response setUserSiteDefault(UserSiteETD siteETD);
}
