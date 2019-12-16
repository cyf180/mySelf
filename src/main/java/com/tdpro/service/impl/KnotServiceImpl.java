package com.tdpro.service.impl;

import com.tdpro.common.constant.KnotType;
import com.tdpro.entity.*;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class KnotServiceImpl implements KnotService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserKnotService userKnotService;
    @Autowired
    private DealLogService dealLogService;
    @Autowired
    private KnotConfigService configService;
    @Autowired
    private UserMonthKnotService monthKnotService;
    private Lock knotLok = new ReentrantLock();

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Future<Boolean> knot(Long orderId) {
        try {
            knotLok.lock();
            POrder order = orderService.findOrderById(orderId);
            if (null != order && !order.getState().equals(0) && order.getIsKnot().equals(new Integer(0)) && order.getZoneType().equals(new Integer(0))) {
                PUser user = userService.findById(order.getUid());
                if (null != user) {
                    if (!user.getStrawUid().equals(0)) {
                        PUser strawUserInfo = userService.findById(user.getStrawUid());
                        if (null != strawUserInfo && !strawUserInfo.getIsUser().equals(new Integer(0))) {
                            switch (order.getIsSuit()) {
                                case 0:
                                    //单品结算
                                    this.itemKnot(order, user, strawUserInfo);
                                    break;
                                case 1:
                                    //套装结算
                                    this.suitKnot(order, user, strawUserInfo);
                                    break;
                                default:
                                    break;
                            }
                        }else{
                            log.info("结算推荐人不是会员，订单ID: {}", order.getId());
                        }
                    } else {
                        log.error("推荐人异常");
                        throw new RuntimeException("推荐人异常");
                    }
                } else {
                    log.info("结算无推人，订单ID: {}", order.getId());
                }
            } else {
                log.error("单品结算订单异常");
            }
        } catch (Exception e) {
            log.error("单品结算锁异常 ", e);
            throw e;
        } finally {
            knotLok.unlock();
        }
        return new AsyncResult<Boolean>(true);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Future<Boolean> monthKnot() {
        int pageNo = 1;
        Date endTime = new Date();
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1;
        rightNow.setTime(endTime);
        rightNow.add(Calendar.MONTH, -1);
        Date startTime = rightNow.getTime();
        this.monthKnotAll(pageNo, startTime, endTime, year, month);
        return new AsyncResult<Boolean>(true);
    }

    private void monthKnotAll(Integer pageNo, Date startTime, Date endTime, Integer year, Integer month) {
        List<PUser> userList = userService.findIsUserList(pageNo);
        if (null != userList && userList.size() > 0) {
            for (int i = 0; i < userList.size(); i++) {
                BigDecimal rate = userList.get(i).getRate();
                Long strawUid = userList.get(i).getId();
                PUserMonthKnot monthKnotFind = monthKnotService.findByUidAndYearAndMonth(strawUid, year, month);
                if (null == monthKnotFind) {
                    List<POrder> orderList = orderService.findUserMonthResultsByPayTime(strawUid, startTime, endTime);
                    List<Long> idList = new ArrayList<>();
                    int orderNum = orderList.size();
                    BigDecimal knotMonthAmount = new BigDecimal("0");
                    BigDecimal newOrderPrice = new BigDecimal("0");
                    if (null != orderList && orderList.size() > 0) {
                        for (int o = 0; o < orderList.size(); o++) {
                            Long orderId = orderList.get(o).getId();
                            Long orderUid = orderList.get(o).getUid();
                            BigDecimal realPrice = orderList.get(o).getRealPrice();
                            BigDecimal knotAmount = realPrice.multiply(rate).setScale(2, BigDecimal.ROUND_DOWN);
                            PUserKnot addKnotLog = userKnotService.insertKnot(strawUid, orderUid, orderId, null, knotAmount, realPrice, KnotType.SUIT_MONTH_KNOT);
                            if (null == addKnotLog) {
                                log.error("月结算失败，添加结算日志失败,订单ID：{},推荐人ID：{}", orderId, strawUid);
                                throw new RuntimeException("月结算失败，添加结算日志失败");
                            }
                            knotMonthAmount = knotMonthAmount.add(knotAmount);
                            newOrderPrice = newOrderPrice.add(realPrice);
                            idList.add(addKnotLog.getId());
                        }
                    }
                    PUserMonthKnot monthKnotAdd = monthKnotService.insertMonthKnot(strawUid, knotMonthAmount, year, month, orderNum, newOrderPrice, rate);
                    boolean updateUser = this.updateUserMonthBalance(userList.get(i), knotMonthAmount);
                    if (!updateUser || null == monthKnotAdd) {
                        log.error("月结算失败，添加月结算日志火修改推荐人余额失败,推荐人ID：{}，修改余额状态：{}，添加月结算日志状态：", strawUid, updateUser, monthKnotAdd);
                        throw new RuntimeException("月结算失败，添加月结算日志火修改推荐人余额失败");
                    }
                    if (null != idList && idList.size() > 0) {
                        if (idList.size() != userKnotService.updateMonthKnotByIdList(idList, monthKnotAdd.getId())) {
                            log.error("月结算失败，关联月结算表失败,推荐人ID：{}", strawUid);
                            throw new RuntimeException("月结算失败，关联月结算表失败");
                        }
                    }
                } else {
                    log.error("月结算失败，推荐人ID：{}，当月已结算", strawUid);
                }
            }
            monthKnotAll(pageNo + 1, startTime, endTime, year, month);
        }
    }

    private boolean updateUserMonthBalance(PUser user, BigDecimal knotMonthAmount) {
        UserBalanceUpdateETD userUPD = new UserBalanceUpdateETD();
        userUPD.setId(user.getId());
        userUPD.setBalance(user.getBalance().add(knotMonthAmount));
        userUPD.setKnotAmount(user.getKnotAmount().add(knotMonthAmount));
        userUPD.setOldBalance(user.getBalance());
        userUPD.setOldKnotAmount(user.getKnotAmount());
        userUPD.setOldItemLeftAmount(user.getItemLeftAmount());
        if (!userService.updateUserBalance(userUPD)) {
            user = userService.findById(user.getId());
            userUPD.setBalance(user.getBalance().add(knotMonthAmount));
            userUPD.setKnotAmount(user.getKnotAmount().add(knotMonthAmount));
            userUPD.setOldBalance(user.getBalance());
            userUPD.setOldKnotAmount(user.getKnotAmount());
            userUPD.setOldItemLeftAmount(user.getItemLeftAmount());
            if (!userService.updateUserBalance(userUPD)) {
                return false;
            }
        }
        return true;
    }

    private void itemKnot(POrder order, PUser user, PUser strawUserInfo) {
        Long orderId = order.getId();
        BigDecimal realPrice = order.getRealPrice();
        if (realPrice.compareTo(new BigDecimal("0")) > 0) {
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
                strawUserUPD.setTeamOneNum(strawUserInfo.getTeamOneNum() + order.getNumber());
                boolean addKnotLog = userKnotService.insertUserKnot(strawUserInfo.getId(), user.getId(), orderId, null, strawKnotAmount, realPrice, KnotType.ITEM_KNOT);
                boolean addDealLog = dealLogService.insertDealLog("单品购买结算", orderId, strawKnotAmount, strawNewBalance, strawUserInfo.getId(), user.getId());
                boolean updateStrawUser = userService.updateUserBalance(strawUserUPD);
                if (!addKnotLog || !updateStrawUser || !addDealLog) {
                    log.error("单品结算失败，订单ID：{},修改推荐人余额 {},添加结算日志 {},添加交易记录 {}", order.getId(), updateStrawUser, updateStrawUser, addDealLog);
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
            log.info("订单实付价格为0");
        }
    }

    private void suitKnot(POrder order, PUser user, PUser strawUserInfo) {
        int allNum = orderService.sumSuitOrderByUid(user.getId(), order.getId());
        boolean plural = false;
        if ((allNum + 1) % 2 == 0) {
            plural = true;
        }
        BigDecimal strawIntegral = strawUserInfo.getIntegral();
        for (int i = 1; i <= order.getNumber(); i++) {
            BigDecimal knotIntegral = new BigDecimal("200");
            if (plural) {
                knotIntegral = new BigDecimal("300");
            }
            boolean addKnotIntegralLog = userKnotService.insertUserKnot(strawUserInfo.getId(), user.getId(), order.getId(), null, knotIntegral, order.getRealPrice(), KnotType.SUIT_ONE_KNOT);
            if (!addKnotIntegralLog) {
                log.error("套装结算，添加结算日志失败，订单ID：{}", order.getId());
                throw new RuntimeException("修改推荐人余额失败");
            }
            if (plural) {
                plural = false;
            } else {
                plural = true;
            }
            strawIntegral = strawIntegral.add(knotIntegral);
        }
        BigDecimal knotIntegral = strawIntegral.divide(new BigDecimal("500"), 0, BigDecimal.ROUND_DOWN);
        BigDecimal strawKnotAmount = knotIntegral.multiply(new BigDecimal("500"));
        BigDecimal strawNewBalance = strawUserInfo.getBalance().add(strawKnotAmount);
        BigDecimal endIntegral = strawIntegral.subtract(strawKnotAmount);
        UserBalanceUpdateETD strawUserUPD = new UserBalanceUpdateETD();
        strawUserUPD.setId(strawUserInfo.getId());
        strawUserUPD.setIntegral(endIntegral);
        strawUserUPD.setTotalIntegral(strawUserInfo.getTotalIntegral().add(strawIntegral));
        strawUserUPD.setTeamSuitNum(strawUserInfo.getTeamSuitNum() + order.getNumber());
        strawUserUPD.setOldTotalIntegral(strawUserInfo.getTotalIntegral());
        strawUserUPD.setOldIntegral(strawUserInfo.getIntegral());
        if (strawKnotAmount.compareTo(new BigDecimal("0")) > 0) {
            strawUserUPD.setOldBalance(strawUserInfo.getBalance());
            strawUserUPD.setBalance(strawNewBalance);
            boolean addDealLog = dealLogService.insertDealLog("积分转换", order.getId(), strawKnotAmount, strawNewBalance, strawUserInfo.getId(), user.getId());
            boolean addLog = userKnotService.insertUserKnot(strawUserInfo.getId(), user.getId(), order.getId(), null, strawKnotAmount, order.getRealPrice(), KnotType.INTEGRAL_SWITCH_KNOT);
            if (!addDealLog || !addLog) {
                log.error("套装结算，积分转换日志添加失败，结算日志：{}，交易记录：{}", addLog, addDealLog);
                throw new RuntimeException("积分转换日志添加失败");
            }
        }
        if (allNum == 0) {
            int newSuitLevelNum = strawUserInfo.getSuitLevelNum() + 1;
            strawUserUPD.setSuitLevelNum(newSuitLevelNum);
            PKnotConfig knotConfig = configService.intervalFind(newSuitLevelNum, strawUserInfo.getKnotId());
            if (null != knotConfig) {
                strawUserUPD.setKnotId(knotConfig.getId());
            }
        }
        boolean updateStraw = userService.updateUserBalance(strawUserUPD);
        POrder orderUPD = new POrder();
        orderUPD.setId(order.getId());
        orderUPD.setIsKnot(1);
        boolean updateOrder = orderService.updateOrder(orderUPD);
        if (!updateStraw || !updateOrder) {
            log.error("套装失败，订单ID：{}，推荐人结算修改状态：{},订单结算状态修改：{}", order.getId(), updateStraw, updateOrder);
            throw new RuntimeException("套装结算，推荐人结算修改状态");
        }
    }
}
