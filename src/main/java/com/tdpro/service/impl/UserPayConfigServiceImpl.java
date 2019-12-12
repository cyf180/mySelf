package com.tdpro.service.impl;

import com.tdpro.entity.PUserPayConfig;
import com.tdpro.mapper.PUserPayConfigMapper;
import com.tdpro.service.UserPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPayConfigServiceImpl implements UserPayConfigService {
    @Autowired
    private PUserPayConfigMapper payConfigMapper;
    @Override
    public PUserPayConfig findByType(Integer type) {
        PUserPayConfig where = new PUserPayConfig();
        where.setType(type);
        return payConfigMapper.findByType(where);
    }
}
