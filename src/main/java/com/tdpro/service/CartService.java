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
     * @param suitName
     * @return
     */
    Boolean insertCart(Long uid, GoodsETD goodsInfo, Long orderId, int num,String suitName);

    List<CartETD> findListByOrderId(Long orderId);
}
