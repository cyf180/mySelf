package com.tdpro.service.impl;

import com.tdpro.entity.PUserMonthKnot;
import com.tdpro.mapper.PUserMonthKnotMapper;
import com.tdpro.service.UserMonthKnotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserMonthKnotServiceImpl implements UserMonthKnotService {
    @Autowired
    private PUserMonthKnotMapper userMonthKnotMapper;
    @Override
    public PUserMonthKnot insertMonthKnot(Long uid, BigDecimal knotPrice, Integer year, Integer month, Integer newOrderNum, BigDecimal newOrderPrice){
        PUserMonthKnot monthKnotADD = new PUserMonthKnot();
        monthKnotADD.setUid(uid);
        monthKnotADD.setKnotPrice(knotPrice);
        monthKnotADD.setYear(year);
        monthKnotADD.setMonth(month);
        monthKnotADD.setNewOrderNum(newOrderNum);
        monthKnotADD.setNewOrderPrice(newOrderPrice);
        monthKnotADD.setCreateTime(new Date());
        if(0 == userMonthKnotMapper.insertSelective(monthKnotADD)){
            return null;
        }
        return monthKnotADD;
    }

    @Override
    public PUserMonthKnot findByUidAndYearAndMonth(Long uid,Integer year,Integer month){
        PUserMonthKnot userMonthKnot = new PUserMonthKnot();
        userMonthKnot.setUid(uid);
        userMonthKnot.setYear(year);
        userMonthKnot.setMonth(month);
        return userMonthKnotMapper.findByUidAndYearAndMonth(userMonthKnot);
    }
}
