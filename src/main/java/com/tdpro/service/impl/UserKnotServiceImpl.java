package com.tdpro.service.impl;

import com.tdpro.common.constant.KnotType;
import com.tdpro.entity.PUserKnot;
import com.tdpro.mapper.PUserKnotMapper;
import com.tdpro.service.UserKnotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserKnotServiceImpl implements UserKnotService {
    @Autowired
    private PUserKnotMapper userKnotMapper;
    @Override
    public Boolean insertUserKnot(Long uid, Long payUid, Long orderId, Long monthId, BigDecimal knotPrice, BigDecimal payPrice, KnotType knotType) {
        PUserKnot userKnotADD = new PUserKnot();
        userKnotADD.setUid(uid);
        userKnotADD.setCreateTime(new Date());
        userKnotADD.setKnotPrice(knotPrice);
        userKnotADD.setKnotType(knotType.getType());
        if(null != payUid){
            userKnotADD.setPayUid(payUid);
        }
        if(null != orderId){
            userKnotADD.setOrderId(orderId);
        }
        if(null != monthId){
            userKnotADD.setMonthId(monthId);
        }
        if(null != payPrice){
            userKnotADD.setPayPrice(payPrice);
        }
        if(0 == userKnotMapper.insertSelective(userKnotADD)){
            return false;
        }
        return true;
    }

    @Override
    public PUserKnot insertKnot(Long uid, Long payUid, Long orderId, Long monthId, BigDecimal knotPrice, BigDecimal payPrice, KnotType knotType){
        PUserKnot userKnotADD = new PUserKnot();
        userKnotADD.setUid(uid);
        userKnotADD.setCreateTime(new Date());
        userKnotADD.setKnotPrice(knotPrice);
        userKnotADD.setKnotType(knotType.getType());
        if(null != payUid){
            userKnotADD.setPayUid(payUid);
        }
        if(null != orderId){
            userKnotADD.setOrderId(orderId);
        }
        if(null != monthId){
            userKnotADD.setMonthId(monthId);
        }
        if(null != payPrice){
            userKnotADD.setPayPrice(payPrice);
        }
        if(0 == userKnotMapper.insertSelective(userKnotADD)){
            return null;
        }
        return userKnotADD;
    }
}
