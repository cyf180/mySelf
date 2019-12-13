package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.GoodsPageETD;
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
@RequestMapping("/api/goods/")
@Api(tags = "运营端-产品相关")
public class AGoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "goodsName", value = "产品名称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "classId", value = "分类Id", required = false, dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "产品列表接口", response = GoodsPageETD.class)
    public Response getList(GoodsPageETD goodsPageETD) {
        return goodsService.adminPageList(goodsPageETD);
    }
}
