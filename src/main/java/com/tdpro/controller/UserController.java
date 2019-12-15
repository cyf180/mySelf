package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.UserEnrollETD;
import com.tdpro.entity.extend.UserTeamETD;
import com.tdpro.entity.extend.UserUPD;
import com.tdpro.service.UserService;
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
@RequestMapping("/user/")
@Api(tags = "用户端 - 用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    Lock updLock = new ReentrantLock();
    Lock enrollLock = new ReentrantLock();

    @GetMapping("userCentre")
    @ApiOperation(value = "会员中心接口")
    public Response getUserCentre(@ApiIgnore @RequestAttribute Long uid){
        return userService.userInformation(uid);
    }

    @GetMapping("teamList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "我的团队列表接口",response = UserTeamETD.class)
    public Response getTeamList(@ApiIgnore @RequestAttribute Long uid,UserTeamETD teamETD){
        teamETD.setId(uid);
        return userService.myTeamList(teamETD);
    }

    @GetMapping("userMaterial")
    @ApiOperation(value = "用户资料接口",response = PUser.class)
    public Response getUserMaterial(@ApiIgnore @RequestAttribute Long uid){
        return userService.userMaterial(uid);
    }

    @PostMapping("updateUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "真实姓名", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bankName", value = "开户银行", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bankBranch", value = "开户支行", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bankCard", value = "银行卡号", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "idCard", value = "身份证", required = false, dataType = "string", paramType = "form"),
    })
    @ApiOperation(value = "修改用户资料")
    public Response updateUser(@ApiIgnore @RequestAttribute Long uid,@Valid @RequestBody UserUPD user){
        Response response;
        try {
            updLock.lock();
            user.setId(uid);
            response = userService.updateUser(user);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updLock.unlock();
        }
        return response;
    }

    @PostMapping("userEnroll")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "payPassword", value = "支付密码", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "strawUid", value = "推荐人id", required = false, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "headPath", value = "推荐人id", required = false, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "nickName", value = "推荐人id", required = false, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "用户注册")
    public Response userEnroll(@ApiIgnore @RequestAttribute Long loginId,@Valid @RequestBody UserEnrollETD user){
        Response response;
        try {
            enrollLock.lock();
            user.setLoginId(loginId);
            response = userService.insertUser(user);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            enrollLock.unlock();
        }
        return response;
    }
}
