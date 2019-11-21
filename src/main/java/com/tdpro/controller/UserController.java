package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.UserTeamETD;
import com.tdpro.service.UserService;
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
@RequestMapping("/user/")
@Api(tags = "用户端 - 用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

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


}
