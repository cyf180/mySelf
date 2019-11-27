package com.tdpro.service;

import com.tdpro.entity.PGoodsSuit;
import com.tdpro.entity.extend.GoodsETD;

public interface CartService {
    /**
     * 添加购物车
     * @param uid
     * @param goodsInfo
     * @param orderId
     * @param num
     * @param suitInfo
     * @return
     */
    Boolean insertCart(Long uid, GoodsETD goodsInfo, Long orderId, int num, PGoodsSuit suitInfo);
}
