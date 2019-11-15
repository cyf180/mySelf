package com.tdpro.common.constant;

/**
 * @About
 * @Author jy
 * @Date 2018/6/12 16:19
 */

public enum BoxType {
    SMALLBOX("smallBox","小箱"),
    BIGBOX("bigBox","大箱");
    String msg;
    String type;
    BoxType(String type,String msg) {
        this.type=type;
        this.msg=msg;
    }
    public static String getResultMsg(String type) {
        String msgInfo = null;
        for (BoxType t : BoxType.values()) {
            if (t.getType().equals(type)) {
                msgInfo=t.getMsg();
                break;
            }
        }
        return msgInfo;
    }
    public String getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
