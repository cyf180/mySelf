package com.tdpro.service.impl;

import com.tdpro.entity.PLog;
import com.tdpro.entity.PUser;
import com.tdpro.mapper.PLogMapper;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private PLogMapper logMapper;
    @Autowired
    private PUserMapper userMapper;
    @Override
    public Boolean insertLog(Long uid, String operation, String note) {
        PUser userInfo = userMapper.selectByPrimaryKey(uid);
        if(null == userInfo){
            return false;
        }
        PLog logADD = new PLog();
        logADD.setUid(uid);
        logADD.setAddName(userInfo.getPhone());
        logADD.setOperation(operation);
        logADD.setNote(note);
        logADD.setCreateTime(new Date());
        if(0 == logMapper.insertSelective(logADD)){
            return false;
        }
        return true;
    }
}
