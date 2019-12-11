package com.tdpro.controller;

import com.alibaba.fastjson.JSONObject;
import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.utils.MenuItem;
import com.tdpro.common.utils.MenuType;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.controller.Base.BaseController;
import com.tdpro.entity.extend.MenuETD;
import com.tdpro.entity.extend.RoleMenuETD;
import com.tdpro.mapper.PRoleMenuMapper;
import com.tdpro.service.Base.BaseService;
import com.tdpro.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;


@RequestMapping("/api/menu")
@RestController
@Api(tags = "后台 - 后台菜单管理Api")
public class MenuController extends BaseController<MenuETD> {
    private static final String MODEL = "0";
    private static final String MODELONE = "1";
    private static final String MODELTWO = "2";
    private static final String MODELTHREE = "3";
    private static final String MODEFOUR = "4";

    @Autowired
    private MenuService menuService;

    @Autowired
    private PRoleMenuMapper roleMenuMapper;

    @Override
    public BaseService<MenuETD> getService() {
        return menuService;
    }


    @PostMapping(value = "/addOrUpdate")
    public Response addOrUpdate(@RequestBody JSONObject jsonObject) throws Exception {
        //选中菜单的信息
        String updateP = jsonObject.getString("updateP");
        Long id = jsonObject.getLong("id");
        String name = jsonObject.getString("menuName");
        String apiUrl = jsonObject.getString("menuApiPath");
        String type = jsonObject.getString("menuType");
        String url = jsonObject.getString("menuUrl");
        //要添加的子菜单
        String nName = jsonObject.getString("n_name");
        String nUrl = jsonObject.getString("n_url");
        String nApiUrl = jsonObject.getString("n_api_url");
        String parentOrChild = jsonObject.getString("parentOrChild");
        String nType = jsonObject.getString("n_type");

        MenuETD itemMenu = null;
        if (nName != null && !"".equals(nName.trim())) {
            itemMenu = new MenuETD();
            //添加子菜单
            if (MODEL.equals(parentOrChild)) {//顶级模块
                itemMenu.setMid(0L);
                itemMenu.setMenuType(MenuType.module.toString());
            } else if (MODELONE.equals(parentOrChild)) {//顶级页面
                itemMenu.setMid(0L);
                itemMenu.setMenuType(MenuType.page.toString());
            } else if (MODELTWO.equals(parentOrChild)) {//子模块
                itemMenu.setMid(id);
                itemMenu.setMenuType(MenuType.module.toString());
            } else if (MODELTHREE.equals(parentOrChild)) {//子页面
                itemMenu.setMid(id);
                itemMenu.setMenuType(MenuType.page.toString());
            } else if (MODEFOUR.equals(parentOrChild)) {   //功能
                itemMenu.setMid(id);
                itemMenu.setMenuType(MenuType.button.toString());
            } else {
                throw new IllegalAccessException("添加菜单异常。");
            }
            itemMenu.setMenuName(nName);
            itemMenu.setMenuUrl(nUrl);
            itemMenu.setMenuType(nType);
            itemMenu.setMenuApiPath(nApiUrl);
        }
        //修改父菜单
        MenuETD m = new MenuETD();
        m.setId(id);
        m.setMenuName(name);
        m.setMenuUrl(url);
        m.setMenuType(type);
        m.setMenuApiPath(apiUrl);
        boolean isTrue=menuService.addOrUpdate(updateP, m, itemMenu);
        if (isTrue){
            return ResponseUtils.successRes("ok");
        }else {
            return ResponseUtils.successRes("false");
        }
    }

    @PostMapping("/getMenusByMid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父级id",required = false,dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "菜单id",required = false,dataType = "string", paramType = "form")
    })
    @ApiOperation(value = "根据父id查询菜单", response = Response.class)
    public Response getMenusByPid(@ApiIgnore @RequestBody JSONObject jsonObject) throws Exception {
        String pid = jsonObject.getString("pid");
        if(pid==null || pid.trim().equals("")) {
            pid = "0";
        }
        String id = jsonObject.getString("id");
        List<MenuItem> menus = menuService.loadMenus(null, pid, "#");

        // 加载全部的菜单
        if(!StringUtils.isEmpty(id)){
            // 加载指定角色的权限
            RoleMenuETD privilege = new RoleMenuETD();
            privilege.setRid(Integer.valueOf(id));
            List<RoleMenuETD> rolePs = roleMenuMapper.selectList(privilege);

            // 拿角色拥有的菜单和全部的菜单做比对，进行勾选
            for (int i = 0; i < rolePs.size(); i++) {
                RoleMenuETD p = rolePs.get(i);
                eeee(p, menus);
            }
        }
        return ResponseUtils.successRes(menus);
    }

    /**
     * 角色权限和资源菜单进行对比，使checkbox选中
     * @param p
     * @param menus
     */
    private void eeee(RoleMenuETD p, List<MenuItem> menus){
        for (int j = 0; j < menus.size(); j++) {
            MenuItem menu = menus.get(j);
            if (p.getMid()!=null&&menu.getId()!=null&&p.getMid().toString().equals(menu.getId().toString())) {
                menu.setChecked(true);
                return;
            }else{
                if(menu.getChildren()!=null && menu.getChildren().size()>0) {
                    eeee(p, menu.getChildren());
                }
            }
        }
    }

    @PostMapping("/deletess")
    @ApiImplicitParam(name = "ids", value = "记录id 多id请用','分割 ",required = true,dataType = "string", paramType = "form")
    @ApiOperation(value = "菜单删除接口", response = Response.class)
    public Response deletess(@ApiIgnore @RequestAttribute("aid") Integer aid, @RequestBody JSONObject jsonObject) {
        Response response = ResponseUtils.successRes(null);
        String ids = jsonObject.getString("ids");
        String deleteParent = jsonObject.getString("deleteParent");
        if(ids != null && !"".equals(ids.trim())) {
            StringBuffer sb = new StringBuffer();
            logger.info(sb.append("用户：").append(aid).append(" 执行批量删除操作 时间：").append(new Date()).toString());
            int a = menuService.deletes(ids,deleteParent);
            if(a==0) {
                response = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_DELETE_ERROR);
            } else {
                response.setData(a);
            }
        } else {
            response = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_DELETE_ERROR);
        }
        return response;
    }
}
