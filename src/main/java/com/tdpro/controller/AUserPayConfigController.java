package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PKnotConfig;
import com.tdpro.entity.PUserPayConfig;
import com.tdpro.entity.extend.UserPayConfigETD;
import com.tdpro.service.UserPayConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/userPay/")
@Api(tags = "运营端 - 用户配置相关")
public class AUserPayConfigController {
    @Autowired
    private UserPayConfigService payConfigService;
    @GetMapping("userPayInfo")
    @ApiOperation(value = "获取配置", response = PKnotConfig.class)
    public Response getKnotConfigList() {
        return payConfigService.getUserConfig();
    }

    @PostMapping("setUserConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userBuy", value = "会员购买价格", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "depositRate", value = "提现利率", required = true, dataType = "BigDecimal", paramType = "form")
    })
    @ApiOperation(value = "配置购买会员价格和提现利率")
    public Response setBuyMember(@ApiIgnore @RequestAttribute("adminId") Long adminId, @Valid @RequestBody UserPayConfigETD payConfig){
        return payConfigService.setBuyMember(payConfig,adminId);
    }
}
