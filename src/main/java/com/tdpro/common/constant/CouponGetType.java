package com.tdpro.common.constant;

/**
 * @About
 * @Author jy
 * @Date 2018/9/18 16:49
 */
public enum  CouponGetType {
    REGISTER(1,"注册后获取"),
    RECHARGE(2,"充值后获取"),
    CONSUM(3,"消费后获取"),
    MANUAL(4,"手动发放"),
    EXCHANGE(5,"兑换码获取"),
    BUY(6,"购买获得");

    String msg;
    int type;
    CouponGetType(Integer type,String msg) {
        this.type=type;
        this.msg=msg;
    }

    public int getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
