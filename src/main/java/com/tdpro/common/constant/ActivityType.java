package com.tdpro.common.constant;

/**
 * @About
 * @Author jy
 * @Date 2019/8/15 10:04
 */
public enum  ActivityType {
    RECHARGE_FREE("4","充值免单"),
    DISCOUNT_COUPON("3","2双鞋洗护抵扣劵"),
    FIRST_ORDER("2","首单3.9"),
    ORDER_NUM("1","洗护数量达到某个值赠送优惠券活动");
    String msg;
    String type;
    ActivityType(String type,String msg) {
        this.type=type;
        this.msg=msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
