package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.entity.PCart;
import com.tdpro.entity.PGoodsSuit;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.CartETD;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.mapper.PCartMapper;
import com.tdpro.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private PCartMapper cartMapper;
    @Override
    public Boolean insertCart(PUser user, GoodsETD goodsInfo, Long orderId, int num, String suitName) {
        Long uid = user.getId();
        PCart cartADD= new PCart();
        cartADD.setUid(uid);
        cartADD.setOrderId(orderId);
        cartADD.setGoodsId(goodsInfo.getId());
        cartADD.setPrice(goodsInfo.getPrice());
        cartADD.setNumber(num);
        cartADD.setGoodsName(goodsInfo.getGoodsName());
        cartADD.setCreateTime(new Date());
        if(StringUtil.isNotEmpty(suitName)) {
            cartADD.setSuitName(suitName);
        }
        if(0 == cartMapper.insertSelective(cartADD)){
            return false;
        }
        return true;
    }

    @Override
    public List<CartETD> findListByOrderId(Long orderId){
        List<CartETD> list = cartMapper.findListByOrderId(orderId);
        if(null != list && list.size() > 0){
            for (CartETD cart:list){
                if(StringUtil.isNotEmpty(cart.getHostImg())){
                    cart.setHostImg(imgPath+cart.getHostImg());
                }
            }
        }
        return list;
    }
}
