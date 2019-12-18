package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PAdmin;
import com.tdpro.entity.PAdvert;
import com.tdpro.entity.extend.AdminHomeETD;
import com.tdpro.entity.extend.AdminPageETD;
import com.tdpro.entity.extend.AdvertETD;
import com.tdpro.service.AdminService;
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
@RequestMapping("/api/admin/")
@Api(tags = "运营端-运营人员")
public class AdminController {
    @Autowired
    private AdminService adminService;
    Lock updLock = new ReentrantLock();

    Lock delLock = new ReentrantLock();

    private Lock updPwdLock = new ReentrantLock();

    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "运营人员列表", response = AdminPageETD.class)
    public Response getAdminList(AdminPageETD adminPageETD) {
        return adminService.pageList(adminPageETD);
    }

    @GetMapping("info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "运营人员详情", response = PAdmin.class)
    public Response getAdminInfo(Long id) {
        return adminService.adminInfo(id);
    }

    @PostMapping("addOrUpdate")
    @ApiOperation(value = "修改")
    public Response getAddOrUpdate(@Valid @RequestBody PAdmin admin){
        Response response;
        try {
            updLock.lock();
            response = adminService.addAdmin(admin);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updLock.unlock();
        }
        return response;
    }

    @PostMapping("display")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "禁用解禁修改")
    public Response displayAdmin(@Valid @RequestBody PAdmin admin){
        Response response;
        try {
            updPwdLock.lock();
            response = adminService.updateState(admin.getId());
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updPwdLock.unlock();
        }
        return response;
    }

    @PostMapping("updPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "form")
    })
    @ApiOperation(value = "禁用解禁修改")
    public Response updPasswod(@ApiIgnore @RequestAttribute("adminId") Long adminId,@Valid @RequestBody PAdmin admin){
        Response response;
        try {
            delLock.lock();
            admin.setId(adminId);
            response = adminService.passwordUpdate(admin);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            delLock.unlock();
        }
        return response;
    }

    @GetMapping("home")
    @ApiOperation(value = "首页", response = AdminHomeETD.class)
    public Response getHome() {
        return adminService.adminHome();
    }
}
