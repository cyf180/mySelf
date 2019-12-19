package com.tdpro.common.constant;

public enum IssueType {
    USER_ASK_TYPE(1,"会员问卷调查"),
    ENROLL_TYPE(0,"注册券发放");
    private Integer type;
    private String msg;
    IssueType(Integer type,String msg){
        this.type=type;
        this.msg=msg;
    }

    public Integer getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
