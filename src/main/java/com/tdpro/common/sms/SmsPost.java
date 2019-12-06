package com.tdpro.common.sms;

import com.tdpro.entity.PSms;
import com.tdpro.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

/**
 * ${DESCRIPTION}
 *
 * @Author cyf
 * @Date 2019/12/6 11:25
 **/

@Service
@Slf4j
public class SmsPost {
    @Autowired
    private SmsService smsService;

    /**
     * 短信发送
     *
     * @param tell
     *            接口帮助类
     * @return
     * @throws IOException
     */
    public boolean sendSmsPost(String tell,String context) throws IOException {
        String[] mobile = new String[1];
        mobile[0]=tell;
        boolean send = huYi(mobile,context);
        return send;
    }

    /**
     * 环信短信接口发送
     * @param mobile
     * @param context
     * @return
     */
//    private boolean huaXin(String[] mobile,String context,CfSms cfSms){
//        if(null == cfSms){
//            return false;
//        }
//        String username = cfSms.getSmsName();
//        String password = cfSms.getSmsPassword();
//        String httpUrl  = cfSms.getSmsHttpUrl();
//        String signature = cfSms.getSmsSigna();
//        try {
//            return MSMSendUtil.sendMsm("",username,password,mobile,context,signature,httpUrl);
//        }catch (Exception e){
//            return false;
//        }
//    }

    /**
     * 互亿短信接口发送
     * @param mobile
     * @param context
     * @return
     */
    private boolean huYi(String[] mobile,String context){
        PSms sms = smsService.findOne();
        //查询短信配置信息
        if (sms==null){
            log.error("互亿短信账号信息未配置");
            return false;
        }
        String apiId = sms.getSmsName();
        String apiKey = sms.getSmsPassword();
        String httpUrlHy  = sms.getSmsHttpUrl();
        try {
            return HySendUtil.sendMsm(apiId,apiKey,mobile,context,httpUrlHy);
        }catch (Exception e){
            return false;
        }

    }
}

