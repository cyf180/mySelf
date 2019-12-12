package com.tdpro.service;

import com.tdpro.entity.PUserPayConfig;

public interface UserPayConfigService {
    /**
     * 类型查找
     * @param type
     * @return
     */
    PUserPayConfig findByType(Integer type);
}
