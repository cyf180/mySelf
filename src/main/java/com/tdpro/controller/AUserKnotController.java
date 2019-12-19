package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserKnotPageETD;
import com.tdpro.entity.extend.UserMonthKnotPageETD;
import com.tdpro.entity.extend.UserPageETD;
import com.tdpro.service.UserKnotService;
import com.tdpro.service.UserMonthKnotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userKnot/")
@Api(tags = "运营端-会员结算")
public class AUserKnotController {
    @Autowired
    private UserKnotService userKnotService;
    @Autowired
    private UserMonthKnotService monthKnotService;
    @GetMapping("knotPageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量 ",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "结算人账号",required = false,dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "payPhone", value = "购买人账号 ",required = false,dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "knotType", value = "结算类型(0:购买积分结算 1:提出结算 2:积分转换)", required = false, dataType = "string", paramType = "query"),
    })
    @ApiOperation(value = "及时计算列表", response = UserKnotPageETD.class)
    public Response getUserPageList(UserKnotPageETD knotPageETD){
        return userKnotService.getAdminList(knotPageETD);
    }

    @GetMapping("monthPageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量 ",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "结算人账号",required = false,dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "及时计算列表", response = UserMonthKnotPageETD.class)
    public Response getMonthPageList(UserMonthKnotPageETD monthKnotPageETD){
        return monthKnotService.getAdminList(monthKnotPageETD);
    }
}
