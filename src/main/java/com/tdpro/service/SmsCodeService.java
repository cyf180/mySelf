package com.tdpro.service;

import java.util.Date;

public interface SmsCodeService {
    /**
     * 添加验证码
     * @param phone
     * @param uid
     * @param code
     * @param addTime
     * @param endTime
     * @return
     */
    Boolean insertSmsCode(String phone, Long uid, String code, Date addTime, Date endTime);

    /**
     * 验证码验证
     * @param phone
     * @param code
     * @return
     */
    Boolean codeVerification(String phone,String code);
}
