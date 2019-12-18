package com.tdpro.service.impl;

import com.tdpro.entity.PGoodsSuit;
import com.tdpro.mapper.PGoodsSuitMapper;
import com.tdpro.service.GoodsSuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsSuitServiceImpl implements GoodsSuitService {
    @Autowired
    private PGoodsSuitMapper goodsSuitMapper;
    @Override
    public PGoodsSuit findById(Long id) {
        return goodsSuitMapper.selectByPrimaryKey(id);
    }

    @Override
    public int countGoodsSuitNum(Long goodsId) {
        return goodsSuitMapper.countByGoodsId(goodsId);
    }

    @Override
    public Boolean delSuitById(Long id) {
        if(0 == goodsSuitMapper.deleteByPrimaryKey(id)){
            return false;
        }
        return true;
    }
}
