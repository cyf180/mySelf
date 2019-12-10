package com.tdpro.service;

import com.tdpro.entity.PUserMonthKnot;

import java.math.BigDecimal;

public interface UserMonthKnotService {
    /**
     * t添加月结日志
     * @param uid
     * @param knotPrice
     * @param year
     * @param month
     * @param newOrderNum
     * @param newOrderPrice
     * @return
     */
    PUserMonthKnot insertMonthKnot(Long uid, BigDecimal knotPrice,Integer year,Integer month,Integer newOrderNum,BigDecimal newOrderPrice,BigDecimal rate);

    /**
     * 查询用户月结详情
     * @param uid
     * @param year
     * @param month
     * @return
     */
    PUserMonthKnot findByUidAndYearAndMonth(Long uid,Integer year,Integer month);
}
