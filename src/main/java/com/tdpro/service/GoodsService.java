package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.GoodsETD;

import java.util.List;

public interface GoodsService {
    /**
     * 产品列表
     * @param goodsETD
     * @return
     */
    List<GoodsETD> goodsList(GoodsETD goodsETD);

    /**
     * 产品详情
     * @param goodsETD
     * @return
     */
    Response goodsInfo(GoodsETD goodsETD);

    /**
     * 商品确认
     * @param id
     * @return
     */
    Response goodsAffirm(Long id,Long uid);

    /**
     * 查询商品
     * @param id
     * @return
     */
    GoodsETD selectInfo(Long id);
}
