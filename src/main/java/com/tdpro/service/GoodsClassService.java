package com.tdpro.service;

import com.tdpro.entity.PGoodsClass;

import java.util.List;

public interface GoodsClassService {
    /**
     * 分类列表
     * @param num
     * @return
     */
    List<PGoodsClass> getList(Integer num);
}
