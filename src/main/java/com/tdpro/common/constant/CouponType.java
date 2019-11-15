package com.tdpro.common.constant;

/**
 * @About
 * @Author jy
 * @Date 2019/10/21 11:34
 */
public enum CouponType {
    DEDUCTION("抵用卷","3"),
    DISCOUNT("折扣券","2"),
    VOUCHER("代金券","1");
    String msg;
    String type;
    CouponType(String msg, String type) {
        this.msg = msg;
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
