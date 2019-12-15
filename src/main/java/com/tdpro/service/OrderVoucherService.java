package com.tdpro.service;

import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.extend.OrderVoucherETD;

import java.util.List;

public interface OrderVoucherService {
    /**
     * 添加使用券
     * @param orderId
     * @param uid
     * @param voucherId
     * @return
     */
    Boolean insertVoucher(Long orderId,Long uid,Long voucherId);

    /**
     * 根据订单id查询使用券列表
     * @param orderId
     * @return
     */
    List<POrderVoucher> findListByOrderId(Long orderId);

    List<OrderVoucherETD> findAdminListByOrderId(Long orderId);
}
