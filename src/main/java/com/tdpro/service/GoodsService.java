package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.POrder;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.entity.extend.GoodsInfoETD;
import com.tdpro.entity.extend.GoodsPageETD;

import java.math.BigDecimal;
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

    /**
     * 修改库存
     * @param order
     * @return
     */
    Boolean updateRepertory(POrder order);

    /**
     * 后台产品列表
     * @param goodsPageETD
     * @return
     */
    Response adminPageList(GoodsPageETD goodsPageETD);

    /**
     * 添加修改产品
     * @param goodsInfoETD
     * @return
     */
    Response updateAndAddGoods(GoodsInfoETD goodsInfoETD);

    /**
     * 修改产品
     * @param goodsInfoETD
     * @return
     */
    Response updateGoods(GoodsInfoETD goodsInfoETD);

    /**
     * 商品上下架
     * @param id
     * @return
     */
    Response updateGoodsIsDel(Long id);

}
