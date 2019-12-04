package com.tdpro.service.impl;

import com.tdpro.entity.PKnotConfig;
import com.tdpro.mapper.PKnotConfigMapper;
import com.tdpro.service.KnotConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnotConfigServiceImpl implements KnotConfigService {
    @Autowired
    private PKnotConfigMapper knotConfigMapper;
    @Override
    public PKnotConfig intervalFind(Integer suitLevelNum,Long id){
        return knotConfigMapper.findBySmallValueAndBigValueAndNotId(suitLevelNum,id);
    }
}
