package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PGoodsClass;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.service.GoodsClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
            @ApiImplicitParam(name = "className", value = "分类名", required = false, dataType = "string", paramType = "query")
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

    @GetMapping("goodsClassInfo")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long", paramType = "query")
    @ApiOperation(value = "所有产品分详情接口", response = PGoodsClass.class)
    public Response goodsClassInfo(Long id) {
        return goodsClassService.classInfo(id);
    }

    @PostMapping("addOrUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类id", required = false, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "className", value = "分类名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "explain", value = "简介", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "iconPath", value = "图标地址", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "isShow", value = "显示与否", required = false, dataType = "int", paramType = "form"),
    })
    @ApiOperation(value = "添加修改产品分类")
    public Response addOrupdateGoodsClass(@Valid @RequestBody PGoodsClass goodsClass){
        return goodsClassService.addOrUpdateClass(goodsClass);
    }

    @PostMapping("updateIsShow")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "添加修改产品分类")
    public Response updateIsShow(@Valid @RequestBody PGoodsClass goodsClass){
        return goodsClassService.updateIsShow(goodsClass);
    }
}
