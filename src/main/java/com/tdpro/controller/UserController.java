package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/user/")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("userCentre")
    @ApiOperation(value = "会员中心接口")
    public Response getUserCentre(@ApiIgnore @RequestAttribute Long uid){
        return userService.userInformation(uid);
    }
}
