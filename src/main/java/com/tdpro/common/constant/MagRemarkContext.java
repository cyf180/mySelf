package com.tdpro.common.constant;

/**
 * @About
 * @Author jy
 * @Date 2018/7/11 17:55
 */
public enum  MagRemarkContext {
    MSG_REMARK_FREE("","为您免费存放24小时，24小时后将收取储衣价"),
    MSG_REMARK("","请24小时内尽快处理订单，否则订单将被取消"),
    PUB_RELEASED("亲爱的懒猫柜族您好，您本次共存衣物@件，洗护工厂收到该衣物时以短信的形式通知您衣物详细信息","感谢您的使用，点击进入小程序，实时查看订单状态"),
    PUB_FACTORY_CHECK("您的衣物已检单完成，即将进入洗涤。如有疑问请于两小时内致电查询。","客服热线：@"),
    PUB_LOGISTICS_PUT("您的衣物洗涤完成，已送回到智能柜，请尽快到柜子前扫码取衣。","将为您免费存放24小时，24小时后将收取储衣价，如有问题，请联系客服@"),
    PUB_CANCEL("您的订单已被取消，请尽快到柜子前扫码取衣。","将为您免费存放24小时，24小时后将收取储衣价，如有问题，请联系客服@"),
    PUB_UNUSUAL("您的订单异常，已被取消，请尽快到柜子前扫码取衣。","将为您免费存放24小时，24小时后将收取储衣价，如有问题，请联系客服@"),
    PUB_PICKUP("您的订单未检单通过，请24小时内进入小程序处理订单，否则订单将被取消。","请进入小程序-订单里面查看并处理，如有疑问请联系客服@"),
    PUB_FIRST_ORDER("您的洗护订单已完成，@元现金已到账，请注意查收。","感谢您的使用，首单用户已返@元现金到账，点击即可查看")
    ;
    String first="";
    String msg="";
    MagRemarkContext(String first,String msg) {
        this.first = first;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
}
