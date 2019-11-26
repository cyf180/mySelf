package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.OrderCartETD;

public interface OrderService {
    /**
     * 添加订单
     * @param orderCartETD
     * @return
     */
    Response insertOrder(OrderCartETD orderCartETD);
}
