package com.tdpro.common.constant;

public enum SmsContent {
    ENROLL_SEND(1,"亲爱的懒猫柜族，您的手机验证码是：@code，该验证码将在5分钟后失效","注册短信发送");
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
