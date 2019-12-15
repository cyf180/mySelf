package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.CollectETD;
import com.tdpro.entity.extend.UserKnotETD;
import com.tdpro.entity.extend.VoucherIssueETD;
import com.tdpro.service.UserKnotService;
import com.tdpro.service.VoucherIssueLogService;
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
@RequestMapping("/user/del/")
@Api(tags = "用户端 - 财务相关")
public class DelLogController {
    @Autowired
    private VoucherIssueLogService issueLogService;
    @Autowired
    private UserKnotService userKnotService;

    @GetMapping("issueList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "用户券收入列表",response = VoucherIssueETD.class)
    public Response getVoucherIssueList(@ApiIgnore @RequestAttribute Long uid){
        VoucherIssueETD issueETD = new VoucherIssueETD();
        issueETD.setUid(uid);
        return issueLogService.userIssueList(issueETD);
    }

    @GetMapping("knotList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "用户结算收入列表",response = UserKnotETD.class)
    public Response getKnotList(@ApiIgnore @RequestAttribute Long uid){
        UserKnotETD issueETD = new UserKnotETD();
        issueETD.setUid(uid);
        return userKnotService.userKnotList(issueETD);
    }
}
