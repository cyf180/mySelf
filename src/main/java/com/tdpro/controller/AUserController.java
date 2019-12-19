package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.UserInfoETD;
import com.tdpro.entity.extend.UserPageETD;
import com.tdpro.entity.extend.UserUPD;
import com.tdpro.service.UserService;
import com.xiaoleilu.hutool.system.UserInfo;
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
@RequestMapping("/api/user/")
@Api(tags = "运营端-管理人员登录")
public class AUserController {
    @Autowired
    private UserService userService;
    Lock updLock = new ReentrantLock();
    Lock agentLock = new ReentrantLock();
    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量 ",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号",required = false,dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 ",required = false,dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "strawUid", value = "上级id", required = false, dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "用户", response = UserPageETD.class)
    public Response getUserPageList(UserPageETD userPage){
        return userService.userPageList(userPage);
    }

    @GetMapping("userInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "会员详情接口", response = UserInfoETD.class)
    public Response getUserInfo(UserInfoETD userInfoETD){
        return userService.userInfo(userInfoETD);
    }

    @PostMapping("updateUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "bankName", value = "开户银行", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bankBranch", value = "开户支行", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bankCard", value = "银行卡号", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "idCard", value = "身份证", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "strawPhone", value = "推荐人账号", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "state", value = "状态", required = false, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "balance", value = "余额", required = false, dataType = "BigDecimal", paramType = "form"),
    })
    @ApiOperation(value = "修改用户资料")
    public Response updateUser(@ApiIgnore @RequestAttribute("adminId") Long adminId,@Valid @RequestBody UserInfoETD user){
        Response response;
        try {
            updLock.lock();
            response = userService.adminUpdateUser(user,adminId);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updLock.unlock();
        }
        return response;
    }

    @PostMapping("updateAgent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "代理设置取消")
    public Response updateAgent(@ApiIgnore @RequestAttribute("adminId") Long adminId,@Valid @RequestBody PUser user){
        Response response;
        try {
            agentLock.lock();
            response = userService.updateUserAgent(user,adminId);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            agentLock.unlock();
        }
        return response;
    }
}
