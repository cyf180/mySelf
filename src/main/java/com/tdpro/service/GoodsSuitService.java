package com.tdpro.service;

import com.tdpro.entity.PGoodsSuit;

public interface GoodsSuitService {
    /**
     * id查询规格
     * @param id
     * @return
     */
    PGoodsSuit findById(Long id);
}
