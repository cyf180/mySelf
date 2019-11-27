package com.tdpro.service;

import com.tdpro.entity.extend.GoodsExchangeETD;

import java.util.List;

public interface GoodsExchangeService {
    /**
     * 产品券配置查询
     * @return
     */
    GoodsExchangeETD selectByGoodsIdAndVoucherId(Long goodsId,Long voucherId);

    /**
     * 产品券配置列表查询
     * @param goodsId
     * @return
     */
    List<GoodsExchangeETD> selectListByGoodsId(Long goodsId);
}
