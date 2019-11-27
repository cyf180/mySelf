package com.tdpro.common.constant;

public enum PayType {
    EXCHANGE_PAY(2,"兑换"),
    WX_PAY(1,"微信支付"),
    BALANCE_PAY(0,"余额支付");
    private Integer type;
    private String msg;
    PayType(Integer type,String msg){
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
