package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserSiteETD;
import com.tdpro.service.UserSiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/site/")
@Api(tags = "用户端 - 收货地址相关接口")
public class UserSiteController {
    @Autowired
    private UserSiteService userSiteService;
    @GetMapping("siteList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "用户收货地址列表",response = UserSiteETD.class)
    public Response getUserSiteList(@ApiIgnore @RequestAttribute Long uid, UserSiteETD siteETD){
        siteETD.setUid(uid);
        return userSiteService.userSiteList(siteETD);
    }

    @PostMapping("delSit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "当前页码", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "用户删除收货地址")
    public Response delUserSit(@ApiIgnore @RequestAttribute Long uid,@Valid @RequestBody UserSiteETD siteETD){
        siteETD.setUid(uid);
        return userSiteService.delUserSite(siteETD);
    }
}
