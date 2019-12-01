package com.tdpro.service;

import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;

import java.util.concurrent.Future;

public interface KnotService {
    /**
     * 单品结算
     * @param orderId
     */
    Future<Boolean> itemKnot(Long orderId);
}
