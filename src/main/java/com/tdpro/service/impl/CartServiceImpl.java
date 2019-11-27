package com.tdpro.service.impl;

import com.tdpro.entity.PCart;
import com.tdpro.entity.PGoodsSuit;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.mapper.PCartMapper;
import com.tdpro.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private PCartMapper cartMapper;
    @Override
    public Boolean insertCart(Long uid, GoodsETD goodsInfo, Long orderId, int num, PGoodsSuit suitInfo) {
        PCart cartADD= new PCart();
        cartADD.setUid(uid);
        cartADD.setOrderId(orderId);
        cartADD.setGoodsId(goodsInfo.getId());
        cartADD.setPrice(goodsInfo.getPrice());
        cartADD.setNumber(num);
        cartADD.setGoodsName(goodsInfo.getGoodsName());
        cartADD.setCreateTime(new Date());
        if(null != suitInfo) {
            cartADD.setSuitId(suitInfo.getId());
            cartADD.setSuitName(suitInfo.getExplain());
        }
        if(0 == cartMapper.insertSelective(cartADD)){
            return false;
        }
        return true;
    }
}
