package com.tdpro.service.impl;

import com.tdpro.entity.extend.GoodsExchangeETD;
import com.tdpro.mapper.PGoodsExchangeMapper;
import com.tdpro.service.GoodsExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsExchangeServiceImpl implements GoodsExchangeService {
    @Autowired
    private PGoodsExchangeMapper exchangeMapper;
    @Override
    public GoodsExchangeETD selectByGoodsIdAndVoucherId(Long goodsId,Long voucherId) {
        return exchangeMapper.selectByGoodsIdAndVoucherId(goodsId,voucherId);
    }

    @Override
    public List<GoodsExchangeETD> selectListByGoodsId(Long goodsId) {
        return exchangeMapper.selectListByGoodsId(goodsId);
    }
}
