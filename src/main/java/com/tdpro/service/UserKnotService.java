package com.tdpro.service;

import com.tdpro.common.constant.KnotType;
import com.tdpro.entity.PUserKnot;

import java.math.BigDecimal;

public interface UserKnotService {
    /**
     * 添加结算记录
     * @param uid
     * @param payUid
     * @param orderId
     * @param monthId
     * @param knotPrice
     * @param payPrice
     * @param knotType
     * @return
     */
    Boolean insertUserKnot(Long uid, Long payUid, Long orderId, Long monthId, BigDecimal knotPrice, BigDecimal payPrice, KnotType knotType);

    PUserKnot insertKnot(Long uid, Long payUid, Long orderId, Long monthId, BigDecimal knotPrice, BigDecimal payPrice, KnotType knotType);
}
