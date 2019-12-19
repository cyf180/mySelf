package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUserAsk;
import com.tdpro.service.UserAskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/user/ask/")
@Api(tags = "用户端 - 用户相关接口")
public class UserAskController {
    @Autowired
    private UserAskService userAskService;
    Lock addLock = new ReentrantLock();

    @PostMapping("keepAsk")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "问卷类容(json字符串)", required = true, dataType = "string", paramType = "form")
    })
    @ApiOperation(value = "问卷添加")
    public Response updateUser(@ApiIgnore @RequestAttribute Long uid, @Valid @RequestBody PUserAsk userAsk){
        Response response;
        try {
            addLock.lock();
            userAsk.setUid(uid);
            response = userAskService.addUserAsk(userAsk);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            addLock.unlock();
        }
        return response;
    }
}
