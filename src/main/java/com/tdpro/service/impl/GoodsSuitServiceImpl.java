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
}