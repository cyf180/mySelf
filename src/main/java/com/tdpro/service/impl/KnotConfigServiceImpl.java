package com.tdpro.service.impl;

import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PKnotConfig;
import com.tdpro.mapper.PKnotConfigMapper;
import com.tdpro.service.KnotConfigService;
import com.tdpro.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class KnotConfigServiceImpl implements KnotConfigService {
    @Autowired
    private PKnotConfigMapper knotConfigMapper;
    @Autowired
    private LogService logService;
    Lock updLock = new ReentrantLock();
    @Override
    public PKnotConfig intervalFind(Integer suitLevelNum,Long id){
        return knotConfigMapper.findBySmallValueAndBigValueAndNotId(suitLevelNum,id);
    }

    @Override
    public Response adminList(){
        return ResponseUtils.successRes(knotConfigMapper.findList());
    }

    @Override
    public Response updateKnotConfig(PKnotConfig knotConfig,Long adminId){
        try {
            updLock.lock();
            if(null == knotConfig.getId()){
                return ResponseUtils.errorRes("关键之错误");
            }
            PKnotConfig knotConfigFind = knotConfigMapper.selectByPrimaryKey(knotConfig.getId());
            if(null == knotConfigFind){
                return ResponseUtils.errorRes("区间不存在");
            }
            if(null == knotConfig.getSmallValue() || knotConfig.getSmallValue().compareTo(new Integer(0)) <= 0){
                return ResponseUtils.errorRes("最小区间错误");
            }
            if(null == knotConfig.getBigValue() || knotConfig.getBigValue().compareTo(new Integer(0)) <= 0){
                return ResponseUtils.errorRes("最大区间错误");
            }
            if(knotConfig.getBigValue().compareTo(knotConfig.getSmallValue()) <= 0){
                return ResponseUtils.errorRes("最大区间不能小于等于最小区间");
            }
            if(knotConfig.getRate().compareTo(new BigDecimal("0")) < 0 ){
                return ResponseUtils.errorRes("提成利率错误，利率不能小于0");
            }
            if(knotConfig.getRate().compareTo(new BigDecimal("1")) >= 0 ){
                return ResponseUtils.errorRes("提成利率错误，利率不能大于1");
            }
            PKnotConfig nextConfig = knotConfigMapper.findLtId(knotConfig.getId());
            if(null != nextConfig){
                int nextBig = nextConfig.getBigValue();
                int atSmall = knotConfig.getSmallValue();
                if(atSmall <= nextBig){
                    return ResponseUtils.errorRes("当前区间值不能小于等于下一级");
                }
            }
            PKnotConfig downConfig = knotConfigMapper.findGtId(knotConfig.getId());
            if(null != downConfig){
                int downSmall = downConfig.getSmallValue();
                int atBig = knotConfig.getBigValue();
                if(atBig >= downSmall){
                    return ResponseUtils.errorRes("当前区间值不能大于等于上一级");
                }
            }
            StringBuffer note = new StringBuffer();
            note.append("修改提成比列最小值：").append(knotConfig.getSmallValue()).append(",最大值：").append(knotConfig.getBigValue()).append(",利率值：").append(knotConfig.getRate())
                    .append(",ID：").append(knotConfig.getId());
            logService.insertLog(adminId,"修改提成利率",note.toString(),1);
            if(0 == knotConfigMapper.updateByPrimaryKeySelective(knotConfig)){
                throw new BusinessException("修改失败");
            }
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }finally {
            updLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }
}
