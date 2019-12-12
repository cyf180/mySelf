package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserPageETD;
import com.tdpro.entity.extend.UserSitePageETD;
import com.tdpro.service.UserSiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userSite/")
@Api(tags = "运营端-用户地址相关")
public class AUserSiteController {
    @Autowired
    private UserSiteService userSiteService;
    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量 ",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "手机号",required = false,dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "衣物流水接口", response = UserPageETD.class)
    public Response getUserPageList(UserSitePageETD sitePageETD){
        return userSiteService.adminUserSiteList(sitePageETD);
    }
}
