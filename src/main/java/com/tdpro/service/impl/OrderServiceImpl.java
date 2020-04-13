package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.constant.PayType;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.*;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.*;
import com.tdpro.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private POrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsExchangeService exchangeService;
    @Autowired
    private UserVoucherService userVoucherService;
    @Autowired
    private GoodsSuitService goodsSuitService;
    @Autowired
    private OrderVoucherService orderVoucherService;
    @Autowired
    private OrderConfigService orderConfigService;
    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;
    private Lock delOrderLock = new ReentrantLock();
    private Lock affirmOrderLock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response insertOrder(OrderAddETD orderCartETD) {
        if (null == orderCartETD.getGoodsId() || null == orderCartETD.getCartAddList() || orderCartETD.getCartAddList().size() <= 0) {
            return ResponseUtils.errorRes("操作异常");
        }
        Long uid = orderCartETD.getUid();
        PUser userInfo = userService.findById(orderCartETD.getUid());
        if(null == userInfo){
            return ResponseUtils.errorRes("用户异常");
        }
        Long goodsId = orderCartETD.getGoodsId();
        GoodsETD goodsInfo = goodsService.selectInfo(goodsId);
        if (null == goodsInfo) {
            return ResponseUtils.errorRes("产品已下架");
        }
        if (goodsInfo.getRepertory().compareTo(new Integer(0)) < 0) {
            return ResponseUtils.errorRes("库存不足");
        }
        int orderNumber = 0;
        if (null != orderCartETD.getCartAddList() && orderCartETD.getCartAddList().size() > 0) {
            List<CartAddETD> cartList = orderCartETD.getCartAddList();
            for (CartAddETD cartAdd : cartList) {
                if(goodsInfo.getIsSuit().equals(new Integer(1))){
                    if(null == cartAdd.getSuitList() || cartAdd.getSuitList().size() < 0){
                        return ResponseUtils.errorRes("请选择规格配置");
                    }
                }
                int num = (null == cartAdd.getNumber() || cartAdd.getNumber() == 0) ? 1 : cartAdd.getNumber();
                if(num < 0){
                    return ResponseUtils.errorRes("数量错误");
                }
                orderNumber = orderNumber + num;
            }
        } else {
            return ResponseUtils.errorRes("操作错误");
        }
        switch (goodsInfo.getZoneType().intValue()) {
            case 1:
                if(userInfo.getIsUser().equals(new Integer(0))){
                    return ResponseUtils.errorRes("您不是会员");
                }
                int num = orderMapper.countByUidAndZoneTypePayNum(uid, 1);
                if (num > 0) {
                    return ResponseUtils.errorRes("您已免费领取过会员产品");
                }
                if (orderNumber > 1) {
                    return ResponseUtils.errorRes("会员专区只能免费领取一个");
                }
                break;
            case 2:
                if(userInfo.getIsUser().equals(new Integer(0))){
                    return ResponseUtils.errorRes("您不是会员");
                }
                List<GoodsExchangeETD> goodsExchangeList = exchangeService.selectListByGoodsId(goodsId);
                if (null == goodsExchangeList || goodsExchangeList.size() <= 0) {
                    return ResponseUtils.errorRes("商品配置异常");
                }
                boolean allow = false;
                for (GoodsExchangeETD exchange : goodsExchangeList) {
                    if (exchange.getNumber().compareTo(new Integer(0)) <= 0) {
                        return ResponseUtils.errorRes("商品兑换配置异常");
                    }
                    Long voucherId = exchange.getVoucherId();
                    int needNum = exchange.getNumber() * orderNumber;
                    List<PUserVoucher> voucherListOne = userVoucherService.selectByUidAndVoucherId(uid, voucherId, needNum);
                    if (null != voucherListOne && voucherListOne.size() >= needNum) {
                        allow = true;
                    }
                }
                if(!allow){
                    return ResponseUtils.errorRes("您的券数量不足");
                }
                break;
        }
        if (goodsInfo.getRepertory().compareTo(new Integer(orderNumber)) < 0) {
            return ResponseUtils.errorRes("库存不足");
        }
        POrder orderADD = getAddOrder(goodsInfo, userInfo, orderNumber);
        Long orderId = orderADD.getId();
        List<CartAddETD> cartList = orderCartETD.getCartAddList();
        int setOf = 1;
        for (CartAddETD cart : cartList) {
            int num = (null == cart.getNumber() || cart.getNumber() == 0) ? 1 : cart.getNumber();
            if (goodsInfo.getIsSuit().equals(new Integer(1)) && null != cart.getSuitList() && cart.getSuitList().size() > 0) {
                int addSuitNum = 0;
                List<Long> suitIds = new ArrayList<>();
                StringBuffer suitName = new StringBuffer();
                for (SuitAddETD suit : cart.getSuitList()) {
                    int addNum = suit.getNumber() == null ? 0 : suit.getNumber();
                    if(addNum < 0){
                        throw new RuntimeException("规格数量错误");
                    }
                    if (null == suit.getId()) {
                        throw new RuntimeException("规格参数异常");
                    }
                    long suiId = suit.getId();
                    if (suitIds.contains(suiId)) {
                        throw new RuntimeException("有重复规格");
                    }
                    suitIds.add(suiId);
                    PGoodsSuit suitInfo = goodsSuitService.findById(suiId);
                    if (null == suitInfo || !suitInfo.getGoodsId().equals(goodsId)) {
                        throw new RuntimeException("规格选择错误");
                    }
                    if (StringUtil.isNotEmpty(suitName.toString())) {
                        suitName.append(",").append(suitInfo.getExplain()).append(" ×").append(addNum);
                    } else {
                        suitName.append(suitInfo.getExplain()).append(" ×").append(addNum);
                    }
                    addSuitNum = addSuitNum +addNum;
                }
                if(5!= addSuitNum){
                    throw new RuntimeException("第"+setOf+"套规格数量错误,规格总数量必须为5");
                }
                if (!cartService.insertCart(userInfo, goodsInfo, orderId, num, suitName.toString())) {
                    throw new RuntimeException("添加购物车失败");
                }
            } else {
                if (!cartService.insertCart(userInfo, goodsInfo, orderId, num, null)) {
                    throw new RuntimeException("添加购物车失败");
                }
            }
            setOf++;
        }
        return ResponseUtils.successRes(orderId);
    }

    @Override
    public POrder findOrderById(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    private POrder getAddOrder(GoodsETD goodsInfo, PUser userInfo, int orderNumber) {
        Long uid = userInfo.getId();
        BigDecimal price = goodsInfo.getPrice();
        if(userInfo.getIsUser().equals(new Integer(1)) && goodsInfo.getVipPrice().compareTo(new BigDecimal("0")) >= 0){
            price = goodsInfo.getVipPrice();//是会员取会员价格
        }
        if(price.compareTo(new BigDecimal("0")) < 0){
            throw new BusinessException("商品价格异常");
        }
        BigDecimal totalPrice = price.multiply(new BigDecimal(orderNumber)).setScale(2, BigDecimal.ROUND_DOWN);
        Long goodsId = goodsInfo.getId();
        POrder orderADD = new POrder();
        orderADD.setUid(uid);
        orderADD.setOrderNo(createOrderNo(uid));
        orderADD.setGoodsId(goodsId);
        orderADD.setGoodsName(goodsInfo.getGoodsName());
        orderADD.setZoneType(goodsInfo.getZoneType());
        orderADD.setIsSuit(goodsInfo.getIsSuit());
        orderADD.setState(0);
        orderADD.setCreateTime(new Date());
        orderADD.setNumber(orderNumber);
        orderADD.setTotalPrice(totalPrice);
        int addOrder = orderMapper.insertSelective(orderADD);
        if (0 == addOrder) {
            throw new RuntimeException("添加订单失败");
        }
        return orderADD;
    }

    @Override
    public Boolean updateOrderIsPay(Long id, PayType payType, String wxOrderNo, BigDecimal callbackPrice, BigDecimal realPrice) {
        POrder orderUPD = new POrder();
        orderUPD.setId(id);
        orderUPD.setState(1);
        orderUPD.setPayTime(new Date());
        orderUPD.setPayType(payType.getType());
        if (null != realPrice) {
            orderUPD.setRealPrice(realPrice);
        }
        if (StringUtil.isNotEmpty(wxOrderNo)) {
            orderUPD.setWxOrderNo(wxOrderNo);
        }
        if (null != callbackPrice) {
            orderUPD.setCallbackPrice(callbackPrice);
        }
        if (0 == orderMapper.updateByPrimaryKeySelective(orderUPD)) {
            return false;
        }
        return true;
    }

    @Override
    public POrder findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    @Override
    public BigDecimal sumRealPrice(Long uid, Long id) {
        return orderMapper.sumRealPrice(uid, id);
    }

    @Override
    public Boolean updateOrder(POrder order) {
        if (0 == orderMapper.updateByPrimaryKeySelective(order)) {
            return false;
        }
        return true;
    }

    @Override
    public int sumSuitOrderByUid(Long uid, Long id) {
        return orderMapper.sumSuitOrderNum(uid, id);
    }

    @Override
    public List<POrder> findUserMonthResultsByPayTime(Long strawUid, Date startTime, Date endTime) {
        return orderMapper.findOrderRealPriceByStrawUid(strawUid, startTime, endTime);
    }

    @Override
    public Future<Boolean> overdueOrder() {
        POrderConfig takeConfig = orderConfigService.findByType(0);
        if (null != takeConfig && takeConfig.getTime() > 0) {
            int day = takeConfig.getTime();
            Calendar takeCa = Calendar.getInstance();
            takeCa.add(Calendar.DATE, -day);
            Date takeTime = takeCa.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = simpleDateFormat.format(takeTime);
            orderMapper.updateOrderNotTake(takeTime);
        } else {
            log.info("订单未确认收货处理，无确认收货到期时间配置");
        }
        POrderConfig payConfig = orderConfigService.findByType(1);
        if (null != payConfig && payConfig.getTime() > 0) {
            int minute = payConfig.getTime();
            Calendar payCa = Calendar.getInstance();
            payCa.add(Calendar.MINUTE, -minute);
            Date payTime = payCa.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = simpleDateFormat.format(payTime);
            List<POrder> orderList = orderMapper.orderNotPayList(payTime);
            if (null != orderList && orderList.size() > 0) {
                for (int i = 0; i < orderList.size(); i++) {
                    long id = orderList.get(i).getId();
                    if (1 == orderMapper.updateOrderNotPay(id)) {
                        userVoucherService.releaseUserVoucher(id);
                    }
                }
            }
        } else {
            log.info("订单未支付处理，无确认收货到期时间配置");
        }
        return new AsyncResult<Boolean>(true);
    }

    @Override
    public Response adminPageList(OrderPageETD orderPageETD) {
        Integer pageNum = orderPageETD.getPageNo() == null ? 1 : orderPageETD.getPageNo();
        Integer pageSize = orderPageETD.getPageSize() == null ? 10 : orderPageETD.getPageSize();
        if (StringUtil.isNotEmpty(orderPageETD.getStartTimes())) {
            try {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                orderPageETD.setStartTime(ft.parse(orderPageETD.getStartTimes()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (StringUtil.isNotEmpty(orderPageETD.getEndTimes())) {
            try {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                orderPageETD.setEndTime(ft.parse(orderPageETD.getEndTimes()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<OrderPageETD> list = orderMapper.findPageList(orderPageETD);
        Map map = new HashMap();
        map.put("pageInfo", new PageInfo(list));
        map.put("queryModel", orderPageETD);
        return ResponseUtils.successRes(map);
    }

    @Override
    public Response adminOrderInfo(Long id) {
        OrderPageETD orderInfo = orderMapper.findOrderInfoById(id);
        if (null == orderInfo) {
            return ResponseUtils.errorRes("订单异常");
        }
        orderInfo.setCardList(cartService.findListByOrderId(orderInfo.getId()));
        List<OrderVoucherETD> voucherList = orderVoucherService.findAdminListByOrderId(orderInfo.getId());
        if (null != voucherList) {
            orderInfo.setVoucherList(voucherList);
        }
        return ResponseUtils.successRes(orderInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response adminUpdateOrder(POrder order, Long adminId) {
        POrder orderFind = orderMapper.selectByPrimaryKey(order.getId());
        if (null == orderFind) {
            return ResponseUtils.errorRes("订单不存在");
        }
        StringBuffer note = new StringBuffer();
        boolean isUpd = false;
        POrder orderUPD = new POrder();
        orderUPD.setId(orderFind.getId());
        if (orderFind.getState().equals(new Integer(1))) {
            if (StringUtil.isEmpty(order.getLogisticsName())) {
                return ResponseUtils.errorRes("请输入物流公司");
            }
            if (StringUtil.isEmpty(order.getLogisticsNo())) {
                return ResponseUtils.errorRes("请输入物流单号");
            }
            note.append("修改订单物流公司：").append(order.getLogisticsName()).append(",物流单号：").append(order.getLogisticsNo());
            orderUPD.setLogisticsName(order.getLogisticsName());
            orderUPD.setLogisticsNo(order.getLogisticsNo());
            orderUPD.setSeendTime(new Date());
            orderUPD.setState(2);
            isUpd = true;
        }
        if (StringUtil.isNotEmpty(order.getBackNote())) {
            note.append(" 订单备注：").append(order.getBackNote());
            orderUPD.setBackNote(order.getBackNote());
            isUpd = true;
        }
        if (isUpd) {
            logService.insertLog(adminId, "订单修改", note.toString(), 1);
            if (0 == orderMapper.updateByPrimaryKeySelective(orderUPD)) {
                throw new BusinessException("修改失败");
            }
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response orderConfig() {
        OrderConfigETD configETD = new OrderConfigETD();
        POrderConfig orderNotPay = orderConfigService.findByType(0);
        if (null != orderNotPay) {
            configETD.setNotPay(orderNotPay.getTime());
        }
        POrderConfig orderNotOk = orderConfigService.findByType(1);
        if (null != orderNotOk) {
            configETD.setNotOk(orderNotOk.getTime());
        }
        return ResponseUtils.successRes(configETD);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response updateOrderConfig(OrderConfigETD orderConfig, Long adminId) {
        if (null == orderConfig.getNotPay() || orderConfig.getNotPay().equals(new Integer(0))) {
            return ResponseUtils.errorRes("未支付超时时间错误");
        }
        POrderConfig configNoPayFind = orderConfigService.findByType(0);
        POrderConfig configPayUPD = new POrderConfig();
        if (null == configNoPayFind) {
            configPayUPD.setType(0);
            configPayUPD.setTime(orderConfig.getNotPay());
            if (!orderConfigService.insertConfig(configPayUPD)) {
                throw new BusinessException("添加失败");
            }
        } else {
            configPayUPD.setId(configNoPayFind.getId());
            configPayUPD.setTime(orderConfig.getNotPay());
            if (!orderConfigService.updateConfig(configPayUPD)) {
                throw new BusinessException("修改失败");
            }
        }
        if (null == orderConfig.getNotOk() || orderConfig.getNotOk().equals(new Integer(0))) {
            return ResponseUtils.errorRes("未收货超时时间错误");
        }
        POrderConfig configNoOkFind = orderConfigService.findByType(1);
        POrderConfig configOkUPD = new POrderConfig();
        if (null == configNoOkFind) {
            configOkUPD.setType(1);
            configOkUPD.setTime(orderConfig.getNotOk());
            if (!orderConfigService.insertConfig(configOkUPD)) {
                throw new BusinessException("添加失败");
            }
        } else {
            configOkUPD.setId(configNoOkFind.getId());
            configOkUPD.setTime(orderConfig.getNotOk());
            if (!orderConfigService.updateConfig(configOkUPD)) {
                throw new BusinessException("修改失败");
            }
        }
        StringBuffer note = new StringBuffer();
        note.append("修改未支付超时时间：").append(orderConfig.getNotPay()).append(",修改未收货超时时间：").append(orderConfig.getNotOk());
        logService.insertLog(adminId, "修改订单配置", note.toString(), 1);
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response userOrderList(OrderETD orderETD) {
        Integer pageNo = orderETD.getPageNo() == null ? 1 : orderETD.getPageNo();
        Integer pageSize = orderETD.getPageSize() == null ? 10 : orderETD.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<OrderETD> siteList = orderMapper.selectListByUid(orderETD);
        if(null != siteList){
            for (OrderETD order:siteList){
                if(StringUtil.isNotEmpty(order.getHostImg())){
                    order.setHostImg(imgPath+order.getHostImg());
                }
            }
        }
        PageInfo pageInfo = new PageInfo(siteList);
        return ResponseUtils.successRes(pageInfo);
    }

    @Override
    public Response userDelOrder(POrder order) {
        try {
            delOrderLock.lock();
            if (null == order.getId()) {
                return ResponseUtils.errorRes("订单异常");
            }
            POrder orderFind = orderMapper.selectByPrimaryKey(order.getId());
            if (null == orderFind) {
                return ResponseUtils.errorRes("订单不存在");
            }
            if (!orderFind.getState().equals(new Integer(0))) {
                return ResponseUtils.errorRes("订单不存在");
            }
            if (!orderFind.getIsDel().equals(new Integer(0))) {
                return ResponseUtils.errorRes("订单不存在");
            }
            if (!orderFind.getUid().equals(order.getUid())) {
                return ResponseUtils.errorRes("操作异常");
            }
            POrder orderUPD = new POrder();
            orderUPD.setId(orderFind.getId());
            orderUPD.setIsDel(-1);
            if (0 == orderMapper.updateByPrimaryKeySelective(orderUPD)) {
                throw new BusinessException("删除失败");
            }
            userVoucherService.releaseUserVoucher(orderFind.getId());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        } finally {
            delOrderLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response affirmOrder(POrder order) {
        try {
            affirmOrderLock.lock();
            if (null == order.getId()) {
                return ResponseUtils.errorRes("订单异常");
            }
            POrder orderFind = orderMapper.selectByPrimaryKey(order.getId());
            if (null == orderFind) {
                return ResponseUtils.errorRes("订单不存在");
            }
            if (!orderFind.getIsDel().equals(new Integer(0))) {
                return ResponseUtils.errorRes("订单不存在");
            }
            if (!orderFind.getState().equals(new Integer(2))) {
                return ResponseUtils.errorRes("该订单未发货");
            }
            if (!orderFind.getUid().equals(order.getUid())) {
                return ResponseUtils.errorRes("操作异常");
            }
            POrder orderUPD = new POrder();
            orderUPD.setId(orderFind.getId());
            orderUPD.setState(3);
            if (0 == orderMapper.updateByPrimaryKeySelective(orderUPD)) {
                throw new BusinessException("收货失败");
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        } finally {
            affirmOrderLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    private String createOrderNo(Long uid) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(uid).append(System.currentTimeMillis());
        return stringBuffer.toString();
    }
}
