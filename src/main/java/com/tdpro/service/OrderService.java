package com.tdpro.service;

import com.tdpro.common.constant.PayType;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.POrder;
import com.tdpro.entity.extend.OrderCartETD;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

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
    Boolean updateOrderIsPay(Long id, PayType payType,String wxOrderNo,BigDecimal callbackPrice);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    POrder findByOrderNo(String orderNo);

    /**
     * 查询用户购买单品金额
     * @param uid
     * @return
     */
    BigDecimal sumRealPrice(Long uid,Long id);

    /**
     * 修改订单
     * @param order
     * @return
     */
    Boolean updateOrder(POrder order);

    /**
     * 查询已支付套装购买数
     * @return
     */
    int sumSuitOrderByUid(Long uid,Long id);

    /**
     * 统计会员月业绩
     * @param startTime
     * @param endTime
     * @return
     */
    List<POrder> findUserMonthResultsByPayTime(Long strawUid, Date startTime, Date endTime);

    /**
     * 过期订单处理
     * @return
     */
    Future<Boolean> overdueOrder();
}
