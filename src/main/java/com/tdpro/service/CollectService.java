package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PCollect;
import com.tdpro.entity.extend.CollectETD;
import com.tdpro.entity.extend.GoodsETD;

public interface CollectService {
    /**
     * 用户收藏列表
     * @param goodsETD
     * @return
     */
    Response userCollect(GoodsETD goodsETD,Long uid);

    /**
     * 收藏删除
     * @param collectETD
     * @return
     */
    Response delCollect(CollectETD collectETD);

    Response addCollect(PCollect collect);
}
