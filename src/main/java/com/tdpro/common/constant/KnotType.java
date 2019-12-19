package com.tdpro.common.constant;

public enum  KnotType {
    DOWN_BUY_MEMBER(4,"下级购买会员奖金结算"),
    ITEM_KNOT(3,"单品购买结算"),
    INTEGRAL_SWITCH_KNOT(2,"积分转换"),
    SUIT_MONTH_KNOT(1,"月提成余额结算"),
    SUIT_ONE_KNOT(0,"套装购买积分结算");
    private Integer type;
    private String msg;
    KnotType(Integer type,String msg){
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
