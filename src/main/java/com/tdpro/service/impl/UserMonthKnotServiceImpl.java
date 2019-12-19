package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUserMonthKnot;
import com.tdpro.entity.extend.UserKnotPageETD;
import com.tdpro.entity.extend.UserMonthKnotPageETD;
import com.tdpro.mapper.PUserMonthKnotMapper;
import com.tdpro.service.UserMonthKnotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMonthKnotServiceImpl implements UserMonthKnotService {
    @Autowired
    private PUserMonthKnotMapper userMonthKnotMapper;
    @Override
    public PUserMonthKnot insertMonthKnot(Long uid, BigDecimal knotPrice, Integer year, Integer month, Integer newOrderNum, BigDecimal newOrderPrice,BigDecimal rate){
        PUserMonthKnot monthKnotADD = new PUserMonthKnot();
        monthKnotADD.setUid(uid);
        monthKnotADD.setKnotPrice(knotPrice);
        monthKnotADD.setYear(year);
        monthKnotADD.setMonth(month);
        monthKnotADD.setNewOrderNum(newOrderNum);
        monthKnotADD.setNewOrderPrice(newOrderPrice);
        monthKnotADD.setCreateTime(new Date());
        monthKnotADD.setRate(rate);
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

    @Override
    public Response getAdminList(UserMonthKnotPageETD monthKnotPageETD){
        Integer pageNum = monthKnotPageETD.getPageNo() == null ? 1 : monthKnotPageETD.getPageNo();
        Integer pageSize = monthKnotPageETD.getPageSize() == null ? 10 : monthKnotPageETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<UserMonthKnotPageETD> list = userMonthKnotMapper.findAdminPageList(monthKnotPageETD);
        Map map = new HashMap();
        map.put("pageInfo",new PageInfo(list));
        map.put("queryModel",monthKnotPageETD);
        return ResponseUtils.successRes(map);
    }
}
