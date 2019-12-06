package com.tdpro.service.impl;

import com.tdpro.entity.PSmsCode;
import com.tdpro.mapper.PSmsCodeMapper;
import com.tdpro.service.SmsCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsCodeServiceImpl implements SmsCodeService {
    @Autowired
    private PSmsCodeMapper smsCodeMapper;
    @Override
    public Boolean insertSmsCode(String phone, Long uid, String code, Date addTime, Date endTime) {
        PSmsCode smsCodeADD = new PSmsCode();
        smsCodeADD.setPhone(phone);
        smsCodeADD.setCode(code);
        if(null != uid){
            smsCodeADD.setUid(uid);
        }
        smsCodeADD.setAddTime(addTime);
        smsCodeADD.setEndTime(endTime);
        if(0 == smsCodeMapper.insertSelective(smsCodeADD)){
            return false;
        }
        return true;
    }

    @Override
    public Boolean codeVerification(String phone, String code) {
        if(null == code){
            return false;
        }
        PSmsCode smsCode = new PSmsCode();
        smsCode.setPhone(phone);
        smsCode.setCode(code);
        PSmsCode codeInfo = smsCodeMapper.findByPhoneAndCode(smsCode);
        if(null == codeInfo){
            return false;
        }
        if(!new Date().before(codeInfo.getEndTime())){
            return false;
        }
        return true;
    }
}
