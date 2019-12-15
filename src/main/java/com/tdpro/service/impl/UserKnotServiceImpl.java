package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.constant.KnotType;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUserKnot;
import com.tdpro.entity.extend.UserKnotETD;
import com.tdpro.entity.extend.VoucherIssueETD;
import com.tdpro.mapper.PUserKnotMapper;
import com.tdpro.service.UserKnotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @Override
    public int updateMonthKnotByIdList(List<Long> idList,Long monthId){
        return userKnotMapper.updateMonthId(idList,monthId);
    }

    @Override
    public Response userKnotList(UserKnotETD userKnotETD){
        Integer pageNo = userKnotETD.getPageNo() == null ? 1 : userKnotETD.getPageNo();
        Integer pageSize = userKnotETD.getPageSize() == null ? 10: userKnotETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<UserKnotETD> siteList = userKnotMapper.selectListByUid(userKnotETD);
        PageInfo pageInfo = new PageInfo(siteList);
        return ResponseUtils.successRes(pageInfo);
    }
}
