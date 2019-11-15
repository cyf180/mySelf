package com.tdpro.common.constant;

/**
 * @About 异常订单处理结果、异常来源
 * @Author jy
 * @Date 2018/6/14 14:08
 */
public enum OrderError {
    UPDATE_RESULT("用户已修改"),
    CANCEL_RESULT("用户已取消订单"),
    AUTO_CANCEL_RESULT("异常订单未处理,已取消"),
    USER_CANCEL_RESULT("异常订单,用户已取消订单"),
    CABINET_SUCCESS_RESULT("已开柜"),
    CABINET_FAIL_RESULT("开柜失败"),
    LOGISTICS_CHECK_SOURCE(0,"物流检单未通过"),
    CABINET_OPEN_APPLY_SOURCE(1,"紧急开柜"),
    FACTORY_CHECK_SOURCE(2,"工厂检单未通过"),
    USER_CANCEL_SOURCE(3,"用户取消订单"),
    ;

    String msg="";
    int type=0;
    OrderError(String msg) {
        this.msg=msg;
    }
    OrderError(int type,String msg) {
        this.type=type;
        this.msg=msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
