package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.CollectETD;
import com.tdpro.service.CollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/collect/")
@Api(tags = "用户端 - 收藏相关接口")
public class CollectController {
    @Autowired
    private CollectService collectService;

    @GetMapping("collectList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示数量", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "用户收藏列表接口",response = CollectETD.class)
    public Response getTeamList(@ApiIgnore @RequestAttribute Long uid, CollectETD collectETD){
        collectETD.setUid(uid);
        return collectService.userCollect(collectETD);
    }

    @PostMapping("delCollect")
    @ApiImplicitParam(name = "id", value = "收藏id", required = true, dataType = "long", paramType = "form")
    @ApiOperation(value = "删除收藏接口")
    public Response delCollect(@ApiIgnore @RequestAttribute Long uid, @Valid @RequestBody CollectETD collectETD){
        collectETD.setUid(uid);
        return collectService.delCollect(collectETD);
    }
}
