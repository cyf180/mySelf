package com.tdpro.service.impl;

import com.tdpro.entity.POrderConfig;
import com.tdpro.mapper.POrderConfigMapper;
import com.tdpro.service.OrderConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderConfigServiceImpl implements OrderConfigService {
    @Autowired
    private POrderConfigMapper orderConfigMapper;
    @Override
    public POrderConfig findByType(Integer type) {
        return orderConfigMapper.findByType(type);
    }

    @Override
    public boolean insertConfig(POrderConfig orderConfig){
        if(0 == orderConfigMapper.insertSelective(orderConfig)){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateConfig(POrderConfig orderConfig){
        if(0 == orderConfigMapper.updateByPrimaryKeySelective(orderConfig)){
            return false;
        }
        return true;
    }
}
