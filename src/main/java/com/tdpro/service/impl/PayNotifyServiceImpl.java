package com.tdpro.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.util.SignUtils;
import com.tdpro.common.constant.IssueType;
import com.tdpro.common.constant.PayType;
import com.tdpro.config.SpringContext;
import com.tdpro.entity.*;
import com.tdpro.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
@Data
@Slf4j
public class PayNotifyServiceImpl implements PayNotifyService {
    @Autowired
    private PayConfigService payConfigService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderVoucherService orderVoucherService;
    @Autowired
    private UserVoucherService userVoucherService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserPayService userPayService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Long orderPayNotify(WxPayOrderNotifyResult resultDate) {
        PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"), new Byte("1"));
        // 验签
        if (resultDate.getSign() != null && !SignUtils.checkSign(resultDate.toMap(), WxPayConstants.TradeType.JSAPI, payConfig.getPaySecret())) {
            log.error("订单支付回调失败 {}", "参数格式校验错误！");
            return new Long(0);
        }
        log.info("订单支付回调结果:{}", resultDate);
        //获取结果
        String attach = resultDate.getAttach();
        String payNo = resultDate.getOutTradeNo();
        String tradeNo = resultDate.getTransactionId();
        BigDecimal totalFee = new BigDecimal(BaseWxPayResult.feeToYuan(resultDate.getTotalFee()));
        String resultCode = resultDate.getResultCode();
        String returnCode = resultDate.getReturnCode();
        String returnMsg = resultDate.getReturnMsg();
        if ("SUCCESS".equals(resultCode) && "SUCCESS".equals(returnCode)) {
            POrder orderInfo = orderService.findByOrderNo(tradeNo);
            if (null != orderInfo) {
                if (orderInfo.getState().equals(0)) {
                    if (!SpringContext.getActiveProfile().equals("dev")) {
                        if (!orderInfo.getRealPrice().equals(totalFee)) {
                            log.error("订单支付回调失败: {}，订单ID: {}", "金额不一致", orderInfo.getId());
                            return new Long(0);
                        }
                    }
                    List<POrderVoucher> orderVoucherList = orderVoucherService.findListByOrderId(orderInfo.getId());
                    if (null != orderVoucherList && orderVoucherList.size() > 0) {
                        if (!userVoucherService.updateUserVoucherIsUseBy(orderVoucherList)) {
                            log.error("订单支付回调失败: {}，订单ID: {}", "优惠券修改失败", orderInfo.getId());
                            throw new RuntimeException("优惠券修改失败");
                        }
                    }
                    boolean updateOrder = orderService.updateOrderIsPay(orderInfo.getId(), PayType.WX_PAY, payNo, totalFee,null);
                    boolean updateGoods = goodsService.updateRepertory(orderInfo);
                    if (!updateOrder || !updateGoods) {
                        log.error("订单支付回调失败: {}，订单ID: ", "订单修改状态：" + updateOrder + "，产品库存修改：" + updateGoods, orderInfo.getId());
                        throw new RuntimeException("订单修改失败");
                    }
                    return orderInfo.getId();
                } else {
                    log.info("订单支付回调成功 {}，订单ID {}", "已支付", orderInfo.getId());
                    return orderInfo.getId();
                }
            } else {
                log.error("订单支付回调失败: {}，订单ID: {}", "订单查询失败", orderInfo.getId());
                return new Long(0);
            }
        } else {
            log.error("订单支付回调失败: {}", returnMsg);
            return new Long(0);
        }
    }

    @Override
    public Boolean userByNotify(WxPayOrderNotifyResult resultDate) {
        PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"), new Byte("1"));
        // 验签
        if (resultDate.getSign() != null && !SignUtils.checkSign(resultDate.toMap(), WxPayConstants.TradeType.JSAPI, payConfig.getPaySecret())) {
            log.error("会员购买支付回调失败 {}", "参数格式校验错误！");
            return false;
        }
        log.info("会员购买回调结果:{}", resultDate);
        String payNo = resultDate.getOutTradeNo();
        String tradeNo = resultDate.getTransactionId();
        BigDecimal totalFee = new BigDecimal(BaseWxPayResult.feeToYuan(resultDate.getTotalFee()));
        String resultCode = resultDate.getResultCode();
        String returnCode = resultDate.getReturnCode();
        String returnMsg = resultDate.getReturnMsg();
        if ("SUCCESS".equals(resultCode) && "SUCCESS".equals(returnCode)) {
            PUserPay payOrderInfo = userPayService.findByNo(tradeNo);
            if (null != payOrderInfo) {
                if (payOrderInfo.getPayState().equals(0)) {
                    if (!SpringContext.getActiveProfile().equals("dev")) {
                        if (!payOrderInfo.getPayPrice().equals(totalFee)) {
                            log.error("会员购买支付回调失败: {}，支付订单ID: {}", "金额不一致", payOrderInfo.getId());
                            throw new RuntimeException("会员购买支付回调金额不一致");
                        }
                    }
                    PUser user = userService.findById(payOrderInfo.getUid());
                    if(null != user) {
                        boolean updateOrder = userPayService.updateIsPay(payOrderInfo.getId(), payNo, totalFee);
                        boolean updateUser = userService.updateIsUser(payOrderInfo.getUid());
                        if (!updateOrder || !updateUser) {
                            log.error("会员购买支付回调失败: {}，支付订单ID: {}", "订单修改状态：" + updateOrder + "，用户修改状态：" + updateUser, payOrderInfo.getId());
                            throw new RuntimeException("订单修改失败");
                        }
                        if(user.getStrawUid().compareTo(new Long(0)) > 0){
                            userVoucherService.voucherIssue(user.getStrawUid(),user.getId(),IssueType.PAY_MEMBER_TYPE);
                        }
                    }else{
                        log.error("会员购买支付回调失败: {}，支付订单ID: {}", "用户查询异常", payOrderInfo.getId());
                        throw new RuntimeException("会员购买支付回调金额不一致");
                    }
                } else {
                    log.info("会员购买支付回调失败 {}，订单ID {}", "已支付", payOrderInfo.getId());
                    return true;
                }
            } else {
                log.error("会员购买支付回调失败: {}，订单号: {}", "支付订单查询失败", tradeNo);
                throw new RuntimeException("订单查询失败");
            }
        } else {
            log.error("会员购买支付回调失败: {}", returnMsg);
            throw new RuntimeException("会员购买支付未成功");
        }
        return true;
    }
}
