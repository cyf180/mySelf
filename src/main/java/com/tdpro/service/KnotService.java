package com.tdpro.service;

import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserPay;

import java.util.concurrent.Future;

public interface KnotService {
    /**
     * 结算
     * @param orderId
     */
    Future<Boolean> knot(Long orderId);

    /**
     * 月结算
     * @return
     */
    Future<Boolean> monthKnot();

    Future<Boolean> buyMemberKnot(PUser user,PUserPay userPay);
}
