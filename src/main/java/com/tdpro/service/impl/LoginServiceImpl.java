package com.tdpro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PAdmin;
import com.tdpro.entity.extend.LoginETD;
import com.tdpro.entity.extend.LoginResultETD;
import com.tdpro.entity.extend.RoleETD;
import com.tdpro.mapper.PRoleMapper;
import com.tdpro.service.AdminService;
import com.tdpro.service.JwtService;
import com.tdpro.service.LoginService;
import com.tdpro.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AdminService tUserService;
    @Autowired
    private PRoleMapper tRoleMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private JwtService jwtService;


    @Override
    public Response adminLogin(LoginETD loginETD) {
        if (loginETD==null|| StringUtils.isEmpty(loginETD.getUserName())||StringUtils.isEmpty(loginETD.getPassWord())){
            return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.PARA_NORULE_ERROR);
        }
        //验证账号密码是否正确
        PAdmin adminWhere=new PAdmin();
        adminWhere.setPhone(loginETD.getUserName());
        adminWhere.setPassword(loginETD.getPassWord());
        PAdmin adminFind=tUserService.findByPhone(adminWhere);
        if (adminFind==null){
            //账号或密码错误
            return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.VALIDATE_USER_ERROR);
        }
        if(!new Integer(0).equals(adminFind.getState())) {
            return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.ACCOUNT_EXIST_ERROR);
        }
        RoleETD role=tRoleMapper.selectByPrimaryKey(adminFind.getRid());
        if (role==null){
            //角色信息错误
            return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SIGN_VALID_ERROR);
        }
        LoginResultETD loginResult=new LoginResultETD();
        //验证通过生成token
        OnlineUserInfo onlineUserInfo=new OnlineUserInfo();
        onlineUserInfo.setUid(adminFind.getId());
        onlineUserInfo.setLoginRole("BACK_ROLE");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("roleId",adminFind.getRid());
        jsonObject.put("userName",adminFind.getName());
        onlineUserInfo.setObject(jsonObject);
        String token=jwtService.createToken(onlineUserInfo);
        if(StringUtils.hasText(token)){
            onlineUserInfo.setToken(token);
            loginResult.setToken(token);
            loginResult.setName(adminFind.getName());
            // 加载允许的菜单信息
            Map map=menuService.loadUserMenu(role.getId());
            loginResult.setMenus(map);
            return ResponseUtils.successRes(loginResult);
        }
        return ResponseUtils.errorRes();
    }
}
