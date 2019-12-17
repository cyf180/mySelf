package com.tdpro.common.constant;

public enum SmsContent {
    ENROLL_SEND(1,"注册验证码：@code,尊敬的用户，您的手机号正在进行芙美人会员注册操作，请妥善保存","注册短信发送");
    private Integer type;
    private String content;
    private String msg;
    SmsContent(Integer type,String content,String msg){
        this.type=type;
        this.content=content;
        this.msg=msg;
    }

    public Integer getType() {
        return type;
    }

    public String getContent(){
        return content;
    }

    public String getMsg() {
        return msg;
    }
}
