package com.tdpro.service;

import com.tdpro.entity.PUserPay;

import java.math.BigDecimal;

public interface UserPayService {
    /**
     * 添加会员购买订单
     * @param uid
     * @param payPrice
     * @return
     */
    PUserPay insertUserPayLog(Long uid, BigDecimal payPrice);

    /**
     * 根据订单号查询
     * @param payNo
     * @return
     */
    PUserPay findByNo(String payNo);

    /**
     * 修改为已支付
     * @return
     */
    Boolean updateIsPay(Long id,String backNo,BigDecimal backPrice);
}
