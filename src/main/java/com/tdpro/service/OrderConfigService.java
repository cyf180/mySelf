package com.tdpro.service;

import com.tdpro.entity.POrderConfig;

public interface OrderConfigService {
    /**
     * 通过type查询
     * @param type
     * @return
     */
    POrderConfig findByType(Integer type);
}
