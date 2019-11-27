package com.tdpro.service.impl;

import com.tdpro.entity.PDealLog;
import com.tdpro.mapper.PDealLogMapper;
import com.tdpro.service.DealLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class DealLogServiceImpl implements DealLogService {
    @Autowired
    private PDealLogMapper dealLogMapper;
    @Override
    public Boolean insertDealLog(String dealName, Long orderId, BigDecimal dealAmount, BigDecimal balance, Long uid, Long bUid) {
        PDealLog dealLogADD = new PDealLog();
        dealLogADD.setDealName(dealName);
        dealLogADD.setUid(uid);
        dealLogADD.setDealAmount(dealAmount);
        dealLogADD.setBalance(balance);
        dealLogADD.setCreateTime(new Date());
        if(null != orderId){
            dealLogADD.setOrderId(orderId);
        }
        if(null != bUid){
            dealLogADD.setbUid(bUid);
        }
        if(0 == dealLogMapper.insertSelective(dealLogADD)){
            return false;
        }
        return true;
    }
}
