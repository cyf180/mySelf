package com.tdpro.service;

import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.OrderPayETD;

public interface PayService {
    /**
     *
     * @param payETD
     * @return
     */
    Response balancePay(OrderPayETD payETD);
}
