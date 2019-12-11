package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.extend.LoginETD;
import com.tdpro.entity.extend.LoginResultETD;
import com.tdpro.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @About 管理员登录
 */
@RestController
@RequestMapping("/api")
@Api(tags = "运营端-管理人员登录")
public class AdminLoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "管理人员登录接口实现",notes = "管理人员登录接口实现",response = LoginResultETD.class)
    @ResponseBody
    public Response getCourierList(@Valid @RequestBody LoginETD loginETD, BindingResult bindingResult) throws Exception{
        Response response = ResponseUtils.handleValidError(bindingResult);
        if (response.getIsOK()){
            response= loginService.adminLogin(loginETD);
        }
        return response;
    }
}
