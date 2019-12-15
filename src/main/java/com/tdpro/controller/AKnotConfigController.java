package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PAdvert;
import com.tdpro.entity.PKnotConfig;
import com.tdpro.entity.extend.AdvertETD;
import com.tdpro.service.KnotConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/knotConfig/")
@Api(tags = "运营端-提成配置相关")
public class AKnotConfigController {
    @Autowired
    private KnotConfigService knotConfigService;
    @GetMapping("pageList")
    @ApiOperation(value = "提成配置列表", response = PKnotConfig.class)
    public Response getKnotConfigList() {
        return knotConfigService.adminList();
    }

    @PostMapping("update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "提成利率id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "提成利率修改")
    public Response getAddOrUpdate(@ApiIgnore @RequestAttribute("adminId") Long adminId, @Valid @RequestBody PKnotConfig knotConfig){
        return knotConfigService.updateKnotConfig(knotConfig,adminId);
    }
}
