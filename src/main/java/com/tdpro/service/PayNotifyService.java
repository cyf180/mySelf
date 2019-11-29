package com.tdpro.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;

public interface PayNotifyService {
    /**
     * 订单微信支付回调
     * @param resultDate
     * @return
     */
    Boolean orderPayNotify(WxPayOrderNotifyResult resultDate);
}
