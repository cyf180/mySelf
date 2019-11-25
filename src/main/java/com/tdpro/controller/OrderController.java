package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.OrderCartETD;
import com.tdpro.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/user/order/")
@Api(tags = "用户端 - 订单相关接口")
public class OrderController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("goodsAffirm")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "商品确认接口",response = OrderCartETD.class)
    public Response getGoodsAffirm(@ApiIgnore @RequestAttribute Long uid, Long id){
        return goodsService.goodsAffirm(id,uid);
    }
}
