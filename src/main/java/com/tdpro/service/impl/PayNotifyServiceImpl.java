package com.tdpro.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.util.SignUtils;
import com.tdpro.common.constant.PayType;
import com.tdpro.config.SpringContext;
import com.tdpro.entity.POrder;
import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PPayConfig;
import com.tdpro.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Boolean orderPayNotify(WxPayOrderNotifyResult resultDate) {
        PPayConfig payConfig = payConfigService.findByChannelAndPayType(new Byte("0"), new Byte("1"));
        // 验签
        if (resultDate.getSign() != null && !SignUtils.checkSign(resultDate.toMap(), WxPayConstants.TradeType.JSAPI, payConfig.getPaySecret())) {
            log.error("订单支付回调失败 {}", "参数格式校验错误！");
            return false;
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
                            throw new RuntimeException("金额不一致");
                        }
                    }
                    List<POrderVoucher> orderVoucherList = orderVoucherService.findListByOrderId(orderInfo.getId());
                    if (null != orderVoucherList && orderVoucherList.size() > 0) {
                        if (!userVoucherService.updateUserVoucherIsUse(orderVoucherList)) {
                            log.error("订单支付回调失败: {}，订单ID: {}", "优惠券修改失败", orderInfo.getId());
                            throw new RuntimeException("优惠券修改失败");
                        }
                    }
                    boolean updateOrder = orderService.updateOrderIsPay(orderInfo.getId(), PayType.WX_PAY, payNo, totalFee);
                    boolean updateGoods = goodsService.updateRepertory(orderInfo);
                    if (!updateOrder || !updateGoods) {
                        log.error("订单支付回调失败: {}，订单ID: ", "订单修改状态：" + updateOrder + "，产品库存修改：" + updateGoods, orderInfo.getId());
                        throw new RuntimeException("订单修改失败");
                    }
                } else {
                    log.info("订单支付回调成功 {}，订单ID {}", "已支付", orderInfo.getId());
                    return true;
                }
            } else {
                log.error("订单支付回调失败: {}，订单ID: {}", "订单查询失败", orderInfo.getId());
                throw new RuntimeException("订单查询失败");
            }
        } else {
            log.error("订单支付回调失败: {}", returnMsg);
            throw new RuntimeException("订单支付未成功");
        }
        return true;
    }
}
