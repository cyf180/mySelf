package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PWithdraw;
import com.tdpro.entity.extend.UserWithdrawETD;
import com.tdpro.entity.extend.WithdrawETD;
import com.tdpro.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/user/withdraw/")
@Api(tags = "用户端 - 用户提现相关接口")
public class WithdrawController {
    @Autowired
    private WithdrawService withdrawService;
    private Lock applyLock = new ReentrantLock();
    @PostMapping("apply")
    @ApiImplicitParam(name = "amount", value = "提现金额", required = true, dataType = "BigDecimal", paramType = "form")
    @ApiOperation(value = "提现申请")
    public Response applyWithdraw(@ApiIgnore @RequestAttribute Long uid, @RequestBody PWithdraw withdrawApplyVo){
        Response response;
        try {
            applyLock.lock();
            withdrawApplyVo.setUid(uid);
            response = withdrawService.withdrawApply(withdrawApplyVo);
        }catch (Exception e){
            throw new RuntimeException("提现申请锁异常");
        }finally {
            applyLock.unlock();
        }
        return response;
    }

    @GetMapping("withdrawList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "柜主提现列表接口",response = WithdrawETD.class)
    public Response getWithdrawList(@ApiIgnore @RequestAttribute Long uid,WithdrawETD withdrawPage){
        withdrawPage.setUid(uid);
        return withdrawService.withdrawList(withdrawPage);
    }

    @GetMapping("withdrawConfig")
    @ApiOperation(value = "提现配置详情接口",response = UserWithdrawETD.class)
    public Response withdrawConfig(@ApiIgnore @RequestAttribute Long uid){
        return withdrawService.userWithdrawInfo(uid);
    }
}
