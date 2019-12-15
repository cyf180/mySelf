package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.POrder;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.entity.extend.GoodsInfoETD;
import com.tdpro.entity.extend.OrderConfigETD;
import com.tdpro.entity.extend.OrderPageETD;
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
@RequestMapping("/api/order/")
@Api(tags = "运营端-订单相关")
public class AOrderController {
    @Autowired
    private OrderService orderService;
    Lock updLock = new ReentrantLock();

    Lock updConfigLock = new ReentrantLock();
    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userPhone", value = "用户账号", required = false, dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "产品分类列表接口", response = GoodsClassPageETD.class)
    public Response getOrderList(OrderPageETD orderPageETD) {
        return orderService.adminPageList(orderPageETD);
    }

    @GetMapping("orderInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单ID", required = true, dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "产品分类列表接口", response = GoodsClassPageETD.class)
    public Response getOrderInfo(Long id) {
        return orderService.adminOrderInfo(id);
    }

    @PostMapping("orderUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "订单修改")
    public Response goodsUpDown(@ApiIgnore @RequestAttribute("adminId") Long adminId,@Valid @RequestBody POrder order){
        Response response;
        try {
            updLock.lock();
            response = orderService.adminUpdateOrder(order,adminId);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updLock.unlock();
        }
        return response;
    }

    @PostMapping("orderConfigUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "订单配置修改")
    public Response orderConfigUpdate(@ApiIgnore @RequestAttribute("adminId") Long adminId,@Valid @RequestBody OrderConfigETD configETD){
        Response response;
        try {
            updConfigLock.lock();
            response = orderService.updateOrderConfig(configETD,adminId);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updConfigLock.unlock();
        }
        return response;
    }

    @GetMapping("orderConfig")
    @ApiOperation(value = "获取订单配置", response = OrderConfigETD.class)
    public Response getOrderConfig(Long id) {
        return orderService.orderConfig();
    }

}
