package com.tdpro.controller;



import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.controller.Base.BaseController;
import com.tdpro.entity.extend.RoleETD;
import com.tdpro.service.Base.BaseService;
import com.tdpro.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/api/role")
@RestController
@Api(tags = "后台角色管理Api")
public class RoleController extends BaseController<RoleETD> {
    @Autowired
    private RoleService roleService;

    @Override
    public BaseService<RoleETD> getService() {
        return roleService;
    }

    @PostMapping("/all")
    @ApiOperation(value = "查询所有角色", response = RoleETD.class)
    public Response<RoleETD> getAllRole() {
        List<RoleETD> list = roleService.selectAllRole();
        Response response = ResponseUtils.successRes(list);
        return response;
    }

    @PostMapping(value="/save")
    @ApiOperation(value = "新增或更新角色", response = Response.class)
    public Response save(@Valid @RequestBody RoleETD role, BindingResult result) {
        Response response = ResponseUtils.handleValidError(result);
        if(response.getIsOK()) {
            roleService.editRole(role);
        }
        return response;
    }

    @PostMapping("/changeStatus")
    @ApiImplicitParam(name = "id", value = "记录id ",required = true,dataType = "int", paramType = "form")
    @ApiOperation(value = "更改角色状态 data=1更新成功", response = Response.class)
    public Response changeStatus(@ApiIgnore @RequestAttribute("aid") Integer aid, Long id) {
        Response response = ResponseUtils.successRes(null);
        int status = roleService.changeStatus(id);
        if(status!=1) {
            response = ResponseUtils.errorRes("更新失败 或没有该角色信息");
        }
        response.setData(status);
        return response;
    }
}
