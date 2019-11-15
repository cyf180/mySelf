package com.tdpro.common.constant;

/**
 * @About 消息推送订单状态
 * @Author jy
 * @Date 2018/7/5 11:23
 */
public enum  MsgContext {
    RELEASED("衣物存放通知","您的衣物已存放成功，等待送往智能工厂洗衣"),
    FACTORY_CHECK("衣物订单确认通知","您的衣物检单完成，正在洗涤中"),
    LOGISTICS_PUT("衣服送达取件通知","您的衣物洗涤完成，已送回到智能柜，请尽快到柜子前扫码取衣"),
    CANCEL("取件通知","您的订单已被取消，请尽快到柜子前扫码取衣"),
    UNUSUAL("取件通知","您的订单异常，已被取消，请尽快到柜子前扫码取衣"),
    PICKUP("异常订单通知","物流检单未通过，点击处理订单"),
    SMS_LOGISTICS_PUT("衣服送达取件通知","亲爱的懒猫柜族，您的洗护订单已完成。请尽快前往小懒猫智能柜扫码取衣/鞋。将为您免费存放24小时，超时将收取储衣/鞋费"),
    SMS_CANCEL_PICKUP("取消订单取件通知","亲爱的懒猫柜族，您的订单已被取消，请尽快到柜子前扫码取衣。将为您免费存放24小时，24小时后将收取储衣价"),
    SMS_UNUSUAL_PICKUP("异常订单取件通知","亲爱的懒猫柜族，您的订单异常，已被取消，请尽快到柜子前扫码取衣。将为您免费存放24小时，24小时后将收取储衣价"),
    SMS_UNUSUAL("异常订单处理通知","亲爱的懒猫柜族，您的订单未检单通过，请24小时内进入小程序处理订单，否则订单将被取消"),
    JPUSH_BOX("空闲箱格紧缺","，柜子箱格紧缺，请尽快去收衣"),
    JPUSH_CABINET("绑定智能柜超时","，您绑定此智能柜已经超时，请尽快去解绑"),
    JPUSH_CABINET_BOX("箱格处于打开中","，有箱格处于打开中，请尽快去关柜"),
    JPUSH_ACCOUNT_PUT("您有新的收衣订单","@，有新的收衣订单，请尽快到柜体前收衣"),
    JPUSH_FACTORY_UNPACK("您有新的送衣订单","@，有新的送衣订单，请尽快到工厂领件送衣"),
    JPUSH_CABINET_OPEN_BOX("有用户提交了紧急开柜申请","，有用户提交了紧急开柜申请，请尽快去处理"),
    SMS_BOX_UNUSUAL("用户支付成功后，开柜失败","亲爱的懒猫柜族，您的订单已支付成功，由于某些不可抗因素，箱格无法打开，请联系物流@，我们将尽快为您处理"),
    COUPON_RECEIVE("优惠劵领取成功通知",""),
    COUPON_EXPIRE("优惠劵到期通知","卡劵还有三天到期，请及时使用！"),
    TIMINAL_OFFLINE("智能柜断线", "智能柜已断线，请尽快处理。断线柜子：@，地址：@。"),
    SMS_FACTORY_CHECK("衣物订单确认通知", "亲爱的懒猫柜族，您的衣物检单完成，即将进入洗涤，订单金额：@元，具体订单详情请进入小懒猫公众号即可查看"),
    SMS_CODE("短信验证码", "亲爱的懒猫柜族，您的手机验证码是：@，该验证码将在5分钟后失效"),
    SMS_FIRSTORDER("首单通知", "亲爱的懒猫柜族 ，您的衣物检单完成，即将进入洗涤，首次下单第一双鞋实付9.9，待洗护完成后，系统会通知您取衣，具体支付金额请参照柜体大屏幕结算页面")
            ;

    String msg="";
    String title="";
    MsgContext(String title,String msg) {
        this.title=title;
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
