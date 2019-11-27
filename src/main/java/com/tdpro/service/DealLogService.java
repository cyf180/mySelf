package com.tdpro.service;

import java.math.BigDecimal;

public interface DealLogService {
    /**
     * 添加交易日志
     * @param dealName
     * @param orderId
     * @param dealAmount
     * @param balance
     * @param uid
     * @param bUid
     * @return
     */
    Boolean insertDealLog(String dealName, Long orderId, BigDecimal dealAmount,BigDecimal balance,Long uid,Long bUid);
}
