package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PKnotConfig;

public interface KnotConfigService {
    /**
     * 区间查询结算比列
     * @param suitLevelNum
     * @return
     */
    PKnotConfig intervalFind(Integer suitLevelNum,Long id);

    Response adminList();

    Response updateKnotConfig(PKnotConfig knotConfig,Long adminId);
}
