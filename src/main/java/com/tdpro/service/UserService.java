package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserTeamETD;

public interface UserService {
    /**
     * 会员中心
     * @param uid
     * @return
     */
    Response userInformation(Long uid);

    /**
     * 我的团队
     * @param userTeamETD
     * @return
     */
    Response myTeamList(UserTeamETD userTeamETD);

    /**
     * 用户资料
     * @param uid
     * @return
     */
    Response userMaterial(Long uid);
}
