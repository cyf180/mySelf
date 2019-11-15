package com.tdpro.common.constant;

/**
 * @About
 * @Author jy
 * @Date 2019/8/1 10:32
 */
public enum SysConfigName {
    WITHDRAW_RATE("柜主提现百分比"),
    CELL_STORAGE("单个箱格储物数量"),
    FIRST_CASHBACK("首单返现"),
    WASH_PROTECT_AVERAGE("洗护均价"),
    SERVICE_PHONE("客服电话"),
    BUY_COUPON("抵用券购买活动状态");
    String name;

    SysConfigName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
