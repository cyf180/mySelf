package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.service.PUserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pro/userLogin/")
@Api(tags = "三方表查询相关接口")
public class PUserLoginController {
    @Autowired
    private PUserLoginService userLoginService;
    @GetMapping("loginInfo")
    @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "long", paramType = "query")
    @ApiOperation(value = "会员微信信息查询接口")
    public Response getUserLogin(Long uid){
        return userLoginService.findUserLogin(uid);
    }
}
