package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserVoucherETD;
import com.tdpro.service.UserVoucherService;
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
@RequestMapping("/user/voucher/")
@Api(tags = "用户端 - 优惠券相关接口")
public class UserVoucherController {
    @Autowired
    private UserVoucherService userVoucherService;

    @GetMapping("voucherList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "优惠券列表",response = UserVoucherETD.class)
    public Response userVouchesList(@ApiIgnore @RequestAttribute Long uid,UserVoucherETD voucherETD){
        voucherETD.setUid(uid);
        return userVoucherService.userVoucherList(voucherETD);
    }
}
