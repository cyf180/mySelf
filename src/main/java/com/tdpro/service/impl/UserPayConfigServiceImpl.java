package com.tdpro.service.impl;

import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUserPayConfig;
import com.tdpro.entity.extend.UserPayConfigETD;
import com.tdpro.mapper.PUserPayConfigMapper;
import com.tdpro.service.LogService;
import com.tdpro.service.UserPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserPayConfigServiceImpl implements UserPayConfigService {
    @Autowired
    private PUserPayConfigMapper payConfigMapper;
    @Autowired
    private LogService logService;
    private Lock buylock = new ReentrantLock();
    private Lock ratelock = new ReentrantLock();

    @Override
    public PUserPayConfig findByType(Integer type) {
        PUserPayConfig where = new PUserPayConfig();
        where.setType(type);
        return payConfigMapper.findByType(where);
    }

    @Override
    public Response setBuyMember(UserPayConfigETD userPayConfigETD, Long adminId) {
        try {
            buylock.lock();
            this.setUserBuy(userPayConfigETD);
            this.setReflectRate(userPayConfigETD);
            StringBuffer note = new StringBuffer();
            note.append("修改会员购买价格为：").append(userPayConfigETD.getUserBuy()).append("修改体现利率为：").append(userPayConfigETD.getDepositRate());
            logService.insertLog(adminId,"会员配置修改",note.toString(),1);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        } finally {
            buylock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    private void setReflectRate(UserPayConfigETD payConfigETD) {
        if (payConfigETD.getDepositRate().compareTo(new BigDecimal("0")) <= 0) {
            throw new BusinessException("利率错误");
        }
        BigDecimal ratePrice = payConfigETD.getDepositRate();
        if (ratePrice.compareTo(new BigDecimal("1")) >= 0) {
            throw new BusinessException("利率不能大于1");
        }
        PUserPayConfig configUPD = new PUserPayConfig();
        configUPD.setPrice(ratePrice);
        PUserPayConfig rate = this.findByType(1);
        if (null == rate) {
            configUPD.setType(0);
            if (0 == payConfigMapper.insertSelective(configUPD)) {
                throw new BusinessException("修改失败");
            }
        } else {
            configUPD.setId(rate.getId());
            if (0 == payConfigMapper.updateByPrimaryKeySelective(configUPD)) {
                throw new BusinessException("修改失败");
            }
        }
    }

    private void setUserBuy(UserPayConfigETD payConfigETD) {
        if (payConfigETD.getUserBuy().compareTo(new BigDecimal("0")) <= 0) {
            throw new BusinessException("会员购买价格错误");
        }
        BigDecimal buyPrice = payConfigETD.getUserBuy();
        PUserPayConfig configUPD = new PUserPayConfig();
        configUPD.setPrice(buyPrice);
        PUserPayConfig userBuy = this.findByType(0);
        if (null == userBuy) {
            configUPD.setType(0);
            if (0 == payConfigMapper.insertSelective(configUPD)) {
                throw new BusinessException("修改失败");
            }
        } else {
            configUPD.setId(userBuy.getId());
            if (0 == payConfigMapper.updateByPrimaryKeySelective(configUPD)) {
                throw new BusinessException("修改失败");
            }
        }
    }

    @Override
    public Response getUserConfig() {
        UserPayConfigETD configETD = new UserPayConfigETD();
        PUserPayConfig userBuy = this.findByType(0);
        if (null != userBuy) {
            configETD.setUserBuy(userBuy.getPrice());
        } else {
            configETD.setUserBuy(new BigDecimal("0"));
        }
        PUserPayConfig userRate = this.findByType(1);
        if (null != userRate) {
            configETD.setDepositRate(userRate.getPrice());
        } else {
            configETD.setDepositRate(new BigDecimal("0"));
        }
        return ResponseUtils.successRes(configETD);
    }
}
