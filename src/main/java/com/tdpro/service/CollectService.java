package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.CollectETD;

public interface CollectService {
    /**
     * 用户收藏列表
     * @param collectETD
     * @return
     */
    Response userCollect(CollectETD collectETD);

    /**
     * 收藏删除
     * @param collectETD
     * @return
     */
    Response delCollect(CollectETD collectETD);
}
