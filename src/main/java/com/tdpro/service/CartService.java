package com.tdpro.service;

import com.tdpro.entity.PGoodsSuit;
import com.tdpro.entity.extend.CartETD;
import com.tdpro.entity.extend.GoodsETD;

import java.util.List;

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

    List<CartETD> findListByOrderId(Long orderId);
}
