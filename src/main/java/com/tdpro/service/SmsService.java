package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PSms;
import com.tdpro.entity.extend.SmsSendETD;

public interface SmsService {
    /**
     * 查询一天短信配置
     * @return
     */
    PSms findOne();

    /**
     * 注册验证码发送
     * @param smsSendETD
     * @return
     */
    Response enrollSmsSend(SmsSendETD smsSendETD);

    Response sendSms();
}
