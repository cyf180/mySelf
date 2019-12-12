package com.tdpro.service.impl;

import com.tdpro.entity.PUserPay;
import com.tdpro.mapper.PUserPayMapper;
import com.tdpro.service.UserPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserPayServiceImpl implements UserPayService {
    @Autowired
    private PUserPayMapper userPayMapper;
    @Override
    public PUserPay insertUserPayLog(Long uid, BigDecimal payPrice) {
        PUserPay userPayADD = new PUserPay();
        userPayADD.setUid(uid);
        userPayADD.setPayPrice(payPrice);
        userPayADD.setCreateTime(new Date());
        userPayADD.setPayNo(this.createPayNo(uid));
        if(0 == userPayMapper.insertSelective(userPayADD)){
            return null;
        }
        return userPayADD;
    }

    @Override
    public PUserPay findByNo(String payNo) {
        PUserPay where = new PUserPay();
        where.setPayNo(payNo);
        return userPayMapper.findByPayNo(where);
    }

    @Override
    public Boolean updateIsPay(Long id, String backNo, BigDecimal backPrice) {
        PUserPay userPayUPD = new PUserPay();
        userPayUPD.setId(id);
        userPayUPD.setBackNo(backNo);
        userPayUPD.setBackPrice(backPrice);
        userPayUPD.setPayState(1);
        userPayUPD.setBackTime(new Date());
        if(0 == userPayMapper.updateByPrimaryKeySelective(userPayUPD)){
            return false;
        }
        return true;
    }

    private String createPayNo(Long uid) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(uid).append(2).append(System.currentTimeMillis());
        return stringBuffer.toString();
    }
}
