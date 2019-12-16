package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUserSite;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/user/site/")
@Api(tags = "用户端 - 收货地址相关接口")
public class UserSiteController {
    @Autowired
    private UserSiteService userSiteService;
    Lock delLock = new ReentrantLock();
    Lock defaultLock = new ReentrantLock();
    Lock addLock = new ReentrantLock();

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

    @PostMapping("delSite")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "用户删除收货地址")
    public Response delUserSit(@ApiIgnore @RequestAttribute Long uid,@Valid @RequestBody UserSiteETD siteETD){
        Response response;
        try {
            delLock.lock();
            siteETD.setUid(uid);
            response = userSiteService.delUserSite(siteETD);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            delLock.unlock();
        }
        return response;
    }

    @PostMapping("defaultSite")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "用户删除收货地址")
    public Response defaultSite(@ApiIgnore @RequestAttribute Long uid,@Valid @RequestBody UserSiteETD siteETD){
        Response response;
        try {
            defaultLock.lock();
            siteETD.setUid(uid);
            response =  userSiteService.setUserSiteDefault(siteETD);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            defaultLock.unlock();
        }
        return response;
    }

    @PostMapping("addSite")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "site", value = "地址详情", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "isDefault", value = "1 默认", required = false, dataType = "int", paramType = "form")
    })
    @ApiOperation(value = "添加收货地址")
    public Response addUserSite(@ApiIgnore @RequestAttribute Long uid,@Valid @RequestBody PUserSite userSite){
        Response response;
        try {
            addLock.lock();
            userSite.setUid(uid);
            response =  userSiteService.userInsertSite(userSite);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            addLock.unlock();
        }
        return response;
    }
}
