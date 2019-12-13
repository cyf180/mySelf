package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.service.GoodsClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/goodsClass/")
@Api(tags = "运营端-产品分类相关")
public class AGoodsClassController {
    @Autowired
    private GoodsClassService goodsClassService;
    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "goodsName", value = "产品名称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "classId", value = "分类Id", required = false, dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "产品分类列表接口", response = GoodsClassPageETD.class)
    public Response getClassList(GoodsClassPageETD classPageETD) {
        return goodsClassService.adminGoodsClassPageList(classPageETD);
    }

    @GetMapping("allList")
    @ApiOperation(value = "所有产品分类列表接口", response = GoodsClassPageETD.class)
    public Response getAllClassList() {
        return goodsClassService.adminClassAllList();
    }
}
