package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.POrder;
import com.tdpro.entity.extend.OrderCartETD;
import com.tdpro.entity.extend.OrderETD;
import com.tdpro.entity.extend.VoucherIssueETD;
import com.tdpro.service.GoodsService;
import com.tdpro.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/user/order/")
@Api(tags = "用户端 - 订单相关接口")
public class OrderController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    private Lock addOrderLock = new ReentrantLock();

    @GetMapping("goodsAffirm")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "订单确认支付接口",response = OrderCartETD.class)
    public Response getGoodsAffirm(@ApiIgnore @RequestAttribute Long uid, Long id){
        return goodsService.goodsAffirm(id,uid);
    }

    @PostMapping("addOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "suitList", value = "套装规格列表", required = true, dataType = "list", paramType = "form"),
            @ApiImplicitParam(name = "suitList.number", value = "数量", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "suitList.id", value = "套装id", required = false, dataType = "long", paramType = "form"),
    })
    @ApiOperation(value = "新增订单")
    public Response insetOrder(@ApiIgnore @RequestAttribute Long uid, @Valid @RequestBody OrderCartETD cartETD){
        Response response;
        try {
            addOrderLock.lock();
            cartETD.setUid(uid);
            response = orderService.insertOrder(cartETD);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            addOrderLock.unlock();
        }
        return response;
    }

    @GetMapping("orderList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "state", value = "0:未支付 1:已支付 2:已发货 3:完成", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "用户券收入列表",response = VoucherIssueETD.class)
    public Response getOrderList(@ApiIgnore @RequestAttribute Long uid, OrderETD orderETD){
        orderETD.setUid(uid);
        return orderService.userOrderList(orderETD);
    }

    @PostMapping("takeOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "form"),
    })
    @ApiOperation(value = "订单确认收货")
    public Response takeOrder(@ApiIgnore @RequestAttribute Long uid, @Valid @RequestBody POrder order){
        order.setUid(uid);
        return orderService.affirmOrder(order);
    }

    @PostMapping("cancelOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "form"),
    })
    @ApiOperation(value = "取消订单")
    public Response cancelOrder(@ApiIgnore @RequestAttribute Long uid, @Valid @RequestBody POrder order){
        order.setUid(uid);
        return orderService.userDelOrder(order);
    }
}
