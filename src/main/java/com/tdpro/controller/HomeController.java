package com.tdpro.controller;

import com.tdpro.common.utils.Page;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.entity.extend.HomeETD;
import com.tdpro.service.AdvertService;
import com.tdpro.service.GoodsClassService;
import com.tdpro.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/home/")
@Api(tags = "用户端 - 首页相关接口")
public class HomeController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private AdvertService advertService;

    @GetMapping("all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "首页接口",response = GoodsETD.class)
    public Response getHome(Page page){
        GoodsETD goodsETD = new GoodsETD();
        goodsETD.setPageNo(page.getPageNo());
        goodsETD.setPageSize(page.getPageSize());
        HomeETD homeETD = new HomeETD();
        homeETD.setAdvertList(advertService.advertList());
        homeETD.setClassList(goodsClassService.getList(new Integer(6)));
        homeETD.setGoodsList(goodsService.goodsList(goodsETD));
        return ResponseUtils.successRes(homeETD);
    }
}
