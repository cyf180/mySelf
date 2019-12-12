package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserInfoETD;
import com.tdpro.entity.extend.UserPageETD;
import com.tdpro.service.UserService;
import com.xiaoleilu.hutool.system.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/user/")
@Api(tags = "运营端-管理人员登录")
public class AUserController {
    @Autowired
    private UserService userService;
    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量 ",required = false,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号",required = false,dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 ",required = false,dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "衣物流水接口", response = UserPageETD.class)
    public Response getUserPageList(@ApiIgnore @RequestAttribute("adminId") Integer adminId, UserPageETD userPage){
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
}
