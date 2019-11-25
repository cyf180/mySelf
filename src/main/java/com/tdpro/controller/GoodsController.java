package com.tdpro.controller;

import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.extend.CollectETD;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.entity.extend.GoodsSuitETD;
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
@RequestMapping("/goods/")
@Api(tags = "用户端 - 商品相关接口")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("goodsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "classId", value = "分类id", required = false, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "zoneType", value = "专区类型(0:普通专区 1:会员专区 2:兑换专区)", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isSuit", value = "是否是套装(0:不是 1:是)", required = false, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "商品列表接口",response = GoodsETD.class)
    public Response getGoodList(GoodsETD goodsETD){
        return ResponseUtils.successRes(new PageInfo(goodsService.goodsList(goodsETD)));
    }

    @GetMapping("goodsInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "商品详情接口",response = GoodsETD.class)
    public Response getGoodInfo(GoodsETD goodsETD){
        return goodsService.goodsInfo(goodsETD);
    }
}
