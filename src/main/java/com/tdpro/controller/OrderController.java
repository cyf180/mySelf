package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.extend.OrderCartETD;
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
            @ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "商品确认接口",response = OrderCartETD.class)
    public Response getGoodsAffirm(@ApiIgnore @RequestAttribute Long uid, Long id){
        return goodsService.goodsAffirm(id,uid);
    }

    @PostMapping("addOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "siteId", value = "收货地址id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "userNote", value = "用户备注", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "voucherId", value = "选择优惠券id", required = false, dataType = "long", paramType = "form"),
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
}
