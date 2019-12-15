package com.tdpro.controller;

import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.exception.NoAuthorizationException;
import com.tdpro.common.model.LoginRequest;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth/")
@Api(tags = "用户端 - 用户授权相关")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信code", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "wxHead", value = "微信头像地址", required = false, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "wxName", value = "微信昵称", required = false, dataType = "string", paramType = "form")
    })
    @ApiOperation(value="小程序端登录授权")
    public Response login(HttpServletResponse resp, @Valid @RequestBody LoginRequest loginRequest, BindingResult inResult) {
        Response response = ResponseUtils.handleValidError(inResult);
        if (response.getIsOK()) {
            response= userService.wxLogin(loginRequest);
        }
        return response;
    }
}
