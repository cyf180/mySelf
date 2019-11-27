package com.tdpro.service;

import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.POrder;
import com.tdpro.entity.extend.OrderCartETD;

public interface OrderService {
    /**
     * 添加订单
     * @param orderCartETD
     * @return
     */
    Response insertOrder(OrderCartETD orderCartETD);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    POrder findOrderById(Long id);

    /**
     * 修改订单已支付
     * @param id
     * @return
     */
    Boolean updateOrderIsPay(Long id, PayType payType);
}
