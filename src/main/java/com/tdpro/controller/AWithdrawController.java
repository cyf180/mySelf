package com.tdpro.controller;

import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.PWithdraw;
import com.tdpro.entity.extend.WithdrawDO;
import com.tdpro.entity.extend.WithdrawPageETD;
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
@RequestMapping("/api/withdraw/")
@Api(tags = "运营端-用户提现相关")
public class AWithdrawController {
    @Autowired
    private WithdrawService withdrawService;
    private Lock auditLock = new ReentrantLock();

    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "提此案状态(-1:失败 0:申请中 1:成功)", required = false, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "提现列表接口", response = WithdrawDO.class)
    public Response getList(WithdrawPageETD withdrawVo) {
        return withdrawService.adminPageList(withdrawVo);
    }

    @PostMapping("withdrawAudit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "页码", required = true, dataType = "int", paramType = "from"),
            @ApiImplicitParam(name = "note", value = "备注", required = false, dataType = "string", paramType = "from"),
            @ApiImplicitParam(name = "status", value = "提此案状态(-1:失败 1:成功)", required = true, dataType = "int", paramType = "from")
    })
    @ApiOperation(value = "提现审核接口")
    public Response withdrawAudit(@ApiIgnore @RequestAttribute("adminId") Long adminId, @RequestBody PWithdraw withdrawVo) {
        try {
            auditLock.lock();
            return withdrawService.updateStatus(withdrawVo,adminId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        } finally {
            auditLock.unlock();
        }
    }

    @GetMapping("selectOne")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "提现记录ID", required = true, dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "提现详情接口", response = PWithdraw.class)
    public Response getInfo(Long id) {
        return withdrawService.adminWithdrawInfo(id);
    }
}
