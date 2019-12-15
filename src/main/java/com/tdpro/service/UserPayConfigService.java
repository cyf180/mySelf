package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PUserPayConfig;
import com.tdpro.entity.extend.UserPayConfigETD;

public interface UserPayConfigService {
    /**
     * 类型查找
     * @param type
     * @return
     */
    PUserPayConfig findByType(Integer type);

    Response setBuyMember(UserPayConfigETD userPayConfigETD, Long adminId);

    Response getUserConfig();
}
