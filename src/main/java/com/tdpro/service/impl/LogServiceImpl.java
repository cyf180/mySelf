package com.tdpro.service.impl;

import com.tdpro.entity.PAdmin;
import com.tdpro.entity.PLog;
import com.tdpro.entity.PUser;
import com.tdpro.mapper.PAdminMapper;
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
    @Autowired
    private PAdminMapper adminMapper;
    @Override
    public Boolean insertLog(Long uid, String operation, String note,int type) {
        PLog logADD = new PLog();
        switch (type){
            case 0:
                PUser userInfo = userMapper.selectByPrimaryKey(uid);
                if(null == userInfo){
                    return false;
                }
                logADD.setAddName(userInfo.getPhone());
                logADD.setType(0);
                break;
            case 1:
                PAdmin admin = adminMapper.selectByPrimaryKey(uid);
                if(null == admin){
                    return false;
                }
                logADD.setAddName(admin.getName());
                logADD.setType(1);
                break;
        }
        logADD.setUid(uid);
        logADD.setOperation(operation);
        logADD.setNote(note);
        logADD.setCreateTime(new Date());
        if(0 == logMapper.insertSelective(logADD)){
            return false;
        }
        return true;
    }
}
