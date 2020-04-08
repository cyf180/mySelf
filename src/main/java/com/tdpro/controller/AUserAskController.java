package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PUserAsk;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.entity.extend.OrderPageETD;
import com.tdpro.entity.extend.UserAskPageETD;
import com.tdpro.service.UserAskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userAsk/")
@Api(tags = "运营端-用户调查问卷")
public class AUserAskController {
    @Autowired
    private UserAskService userAskService;

    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "列表接口", response = UserAskPageETD.class)
    public Response getPageList(UserAskPageETD userAskPageETD) {
        return userAskService.adminPageList(userAskPageETD);
    }

    @GetMapping("info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = true, dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "详情接口", response = UserAskPageETD.class)
    public Response getInfo(Long uid) {
        return userAskService.info(uid);
    }
}
