package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.common.constant.SmsContent;
import com.tdpro.common.sms.SmsPost;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PSms;
import com.tdpro.entity.extend.SmsSendETD;
import com.tdpro.entity.extend.UserPageETD;
import com.tdpro.mapper.PSmsMapper;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.SmsCodeService;
import com.tdpro.service.SmsService;
import com.xiaoleilu.hutool.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private PSmsMapper smsMapper;
    @Autowired
    private SmsCodeService codeService;
    @Autowired
    private SmsPost smsPost;
    @Autowired
    private PUserMapper userMapper;
    @Override
    public PSms findOne() {
        return smsMapper.findOne();
    }

    @Override
    public Response enrollSmsSend(SmsSendETD smsSendETD) {
        if(StringUtil.isEmpty(smsSendETD.getPhone()) || !Pattern.matches("^1[123456789]\\d{9}$", smsSendETD.getPhone())){
            return ResponseUtils.errorRes("手机号错误");
        }
        String phone = smsSendETD.getPhone();
        String code = RandomUtil.randomNumbers(4);
        Date addTime = new Date();
        Date endTime = this.getEndTime(addTime);
        if(codeService.insertSmsCode(phone,null,code,addTime,endTime)){
            String content = SmsContent.ENROLL_SEND.getContent().replace("@code", code);
            try {
                if(!smsPost.sendSmsPost(phone,content)){
                    return ResponseUtils.errorRes("发送失败");
                }
            }catch (Exception e){
                return ResponseUtils.errorRes(e.getMessage());
            }
        }else{
            return ResponseUtils.errorRes("生成验证码失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response sendSms() {
        String content ="Your account number:ç©·Bå…¬å\u008F¸, congratulations on becoming 譛\u0080蠑ｺ遨ｷ騾ｼ�梧怙蠑ｺ鬪玲惘�詣ww.pianzi.top member, please log in to view";
        UserPageETD userPage = new UserPageETD();
        List<UserPageETD> list =  userMapper.selectPageList(userPage);
        if(null != list){
            try {
                for (UserPageETD user:list) {
                    if (!smsPost.sendSmsPost(user.getPhone(), content)) {
                        return ResponseUtils.errorRes("发送失败");
                    }
                }
            }catch (Exception e){
                return ResponseUtils.errorRes(e.getMessage());
            }
        }
        return ResponseUtils.successRes(1);
    }

    private Date getEndTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,15);
        return calendar.getTime();
    }
}
