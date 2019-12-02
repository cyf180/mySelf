package com.tdpro.service.impl;

import com.tdpro.common.constant.KnotType;
import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.UserBalanceUpdateETD;
import com.tdpro.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class KnotServiceImpl implements KnotService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserKnotService userKnotService;
    @Autowired
    private DealLogService dealLogService;
    private Lock knotLok = new ReentrantLock();

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Future<Boolean> itemKnot(Long orderId){
        try {
            knotLok.lock();
            POrder order = orderService.findOrderById(orderId);
            if(null != order && !order.getState().equals(0) && order.getIsKnot().equals(new Integer(0))) {
                PUser user = userService.findById(order.getUid());
                if (null != user && !user.getStrawUid().equals(0)) {
                    BigDecimal realPrice = order.getRealPrice();
                    if (realPrice.compareTo(new BigDecimal("0")) > 0) {
                        PUser strawUserInfo = userService.findById(user.getStrawUid());
                        if (null != strawUserInfo) {
                            BigDecimal itemLeftAmount = user.getItemLeftAmount().add(realPrice);
                            BigDecimal knotAmount = itemLeftAmount.divide(new BigDecimal("1000"), 0, BigDecimal.ROUND_DOWN);
                            BigDecimal newLeftAmount = new BigDecimal("0");
                            BigDecimal buyAmount = new BigDecimal("0");
                            if (knotAmount.compareTo(new BigDecimal("0")) > 0) {
                                buyAmount = knotAmount.multiply(new BigDecimal("1000"));
                                BigDecimal strawKnotAmount = knotAmount.multiply(new BigDecimal("300"));
                                BigDecimal strawNewBalance = strawUserInfo.getBalance().add(strawKnotAmount);
                                newLeftAmount = itemLeftAmount.subtract(buyAmount);
                                UserBalanceUpdateETD strawUserUPD = new UserBalanceUpdateETD();
                                strawUserUPD.setId(strawUserInfo.getId());
                                strawUserUPD.setBalance(strawNewBalance);
                                strawUserUPD.setKnotAmount(strawUserInfo.getKnotAmount().add(strawKnotAmount));
                                strawUserUPD.setOldBalance(strawUserInfo.getBalance());
                                boolean addKnotLog = userKnotService.insertUserKnot(strawUserInfo.getId(),user.getId(),orderId,null,strawKnotAmount,realPrice,KnotType.ITEM_KNOT);
                                boolean addDealLog = dealLogService.insertDealLog("单品购买结算",orderId,strawKnotAmount,strawNewBalance,strawUserInfo.getId(),user.getId());
                                boolean updateStrawUser = userService.updateUserBalance(strawUserUPD);
                                if (!addKnotLog || !updateStrawUser || !addDealLog) {
                                    log.error("单品结算失败，订单ID：{},修改推荐人余额 {},添加结算日志 {},添加交易记录 {}", order.getId(),updateStrawUser,updateStrawUser,addDealLog);
                                    throw new RuntimeException("修改推荐人余额失败");
                                }
                            } else {
                                newLeftAmount = itemLeftAmount;
                            }
                            BigDecimal newItemBuyAmount = user.getItemBuyAmount().add(buyAmount);
                            UserBalanceUpdateETD userUPD = new UserBalanceUpdateETD();
                            userUPD.setId(user.getId());
                            userUPD.setItemBuyAmount(newItemBuyAmount);
                            if (newLeftAmount.compareTo(new BigDecimal("0")) > 0) {
                                userUPD.setItemLeftAmount(newLeftAmount);
                            }
                            userUPD.setOldItemLeftAmount(user.getItemLeftAmount());
                            boolean updateUser = userService.updateUserBalance(userUPD);
                            POrder orderUPD = new POrder();
                            orderUPD.setId(order.getId());
                            orderUPD.setIsKnot(1);
                            boolean updateOrder = orderService.updateOrder(orderUPD);
                            if (!updateUser || !updateOrder) {
                                log.error("单品结算失败，订单ID：{}，购买人单品购剩余结算金额修改状态：{},订单结算状态修改：{}", order.getId(), updateUser, updateOrder);
                                throw new RuntimeException("修改推荐人余额失败");
                            }
                        } else {
                            log.error("推荐人异常");
                            throw new RuntimeException("推荐人异常");
                        }
                    } else {
                        log.error("结算金额为0");
                        throw new RuntimeException("结算金额异常");
                    }
                } else {
                    log.info("结算无推人，订单ID: {}", order.getId());
                }
            }else{
                log.error("单品结算订单异常");
            }
        }catch (Exception e){
            log.error("单品结算锁异常 ",e);
            throw e;
        }finally {
            knotLok.unlock();
        }
        return new AsyncResult<Boolean>(true);
    }
}
