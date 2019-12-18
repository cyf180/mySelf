package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PAdmin;
import com.tdpro.entity.PRole;
import com.tdpro.entity.extend.AdminHomeETD;
import com.tdpro.entity.extend.AdminPageETD;
import com.tdpro.entity.extend.AdvertETD;
import com.tdpro.entity.extend.RoleETD;
import com.tdpro.mapper.PAdminMapper;
import com.tdpro.mapper.POrderMapper;
import com.tdpro.mapper.PRoleMapper;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.AdminService;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private PAdminMapper adminMapper;
    @Autowired
    private PRoleMapper roleMapper;
    @Autowired
    private POrderMapper orderMapper;
    @Autowired
    private PUserMapper userMapper;
    private Lock updLock = new ReentrantLock();
    private Lock updPwdLock = new ReentrantLock();

    @Override
    public PAdmin findByPhone(PAdmin admin) {
        if (StringUtil.isEmpty(admin.getPhone()) || StringUtil.isEmpty(admin.getPassword())) {
            return null;
        }
        String pwd = DigestUtil.md5Hex(admin.getPassword());
        admin.setPassword(pwd);
        return adminMapper.findByPhoneAndPassword(admin);
    }

    @Override
    public PAdmin findById(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public Response pageList(AdminPageETD adminPageETD) {
        Integer pageNum = adminPageETD.getPageNo() == null ? 1 : adminPageETD.getPageNo();
        Integer pageSize = adminPageETD.getPageSize() == null ? 10 : adminPageETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<AdminPageETD> list = adminMapper.findPageList(adminPageETD);
        Map map = new HashMap();
        map.put("pageInfo", new PageInfo(list));
        map.put("queryModel", adminPageETD);
        return ResponseUtils.successRes(map);
    }

    @Override
    public Response addAdmin(PAdmin admin) {
        if (StringUtil.isEmpty(admin.getName())) {
            return ResponseUtils.errorRes("请输入姓名");
        }
        if (null == admin.getRid() || admin.getRid().equals(new Long(0))) {
            return ResponseUtils.errorRes("请选择角色");
        }
        RoleETD role = roleMapper.selectByPrimaryKey(admin.getRid());
        if (null == role) {
            return ResponseUtils.errorRes("角色不存在");
        }
        int six = admin.getSix() == null ? 0 : admin.getSix();
        int state = admin.getState() == null ? 0 : admin.getState();
        PAdmin adminUPD = new PAdmin();
        if(StringUtil.isNotEmpty(admin.getPassword())){
            String pwd = DigestUtil.md5Hex(admin.getPassword());
            adminUPD.setPassword(pwd);
        }
        adminUPD.setName(admin.getName());
        adminUPD.setSix(six);
        adminUPD.setRid(role.getId());
        adminUPD.setState(state);
        if (null == admin.getId()) {
            if (StringUtil.isEmpty(admin.getPhone()) || !Pattern.matches("^1[123456789]\\d{9}$", admin.getPhone())) {
                return ResponseUtils.errorRes("手机号错误");
            }
            if (StringUtil.isEmpty(admin.getPassword())) {
                return ResponseUtils.errorRes("请输入登录密码");
            }
            PAdmin adminFind = adminMapper.findByPhone(admin.getPhone());
            if (null == adminFind) {
                adminUPD.setPhone(admin.getPhone());
                adminUPD.setCreateTime(new Date());
                if (0 == adminMapper.insertSelective(adminUPD)) {
                    throw new BusinessException("添加失败");
                }
            } else {
                return ResponseUtils.errorRes("账号已存在");
            }
        } else {
            PAdmin adminInfo = adminMapper.selectByPrimaryKey(admin.getId());
            if (null == adminInfo) {
                return ResponseUtils.errorRes("管理员不存在");
            }
            adminUPD.setId(adminInfo.getId());
            if (state == 0) {
                adminUPD.setLiftingTime(new Date());
            } else {
                adminUPD.setDisableTime(new Date());
            }
            if (0 == adminMapper.updateByPrimaryKeySelective(adminUPD)) {
                throw new BusinessException("修改失败");
            }
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response updateState(Long id) {
        if (null == id) {
            return ResponseUtils.errorRes("关键之错误");
        }
        PAdmin adminInfo = adminMapper.selectByPrimaryKey(id);
        if (null == adminInfo) {
            return ResponseUtils.errorRes("管理员不存在");
        }
        PAdmin adminUPD = new PAdmin();
        adminUPD.setId(adminInfo.getId());
        int state = 0;
        if (adminInfo.getState().equals(new Integer(0))) {
            state = -1;
            adminUPD.setDisableTime(new Date());
        } else {
            adminUPD.setLiftingTime(new Date());
        }
        adminUPD.setState(state);
        if (0 == adminMapper.updateByPrimaryKeySelective(adminUPD)) {
            throw new BusinessException("失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response adminInfo(Long id) {
        return ResponseUtils.successRes(adminMapper.selectByPrimaryKey(id));
    }

    @Override
    public Response passwordUpdate(PAdmin admin) {
        if (StringUtil.isEmpty(admin.getPassword())) {
            return ResponseUtils.errorRes("请输入密码");
        }
        PAdmin adminUPD = new PAdmin();

        PAdmin adminFid = adminMapper.selectByPrimaryKey(admin.getId());
        if (null == adminFid) {
            return ResponseUtils.errorRes("管理员不存在");
        }
        if (StringUtil.isNotEmpty(admin.getPassword())) {
            if(StringUtil.isEmpty(admin.getOldPassword())){
                return ResponseUtils.errorRes("请输入旧密码");
            }
            String oldPwd = DigestUtil.md5Hex(admin.getOldPassword());
            if(!oldPwd.equals(adminFid.getPassword())){
                return ResponseUtils.errorRes("旧密码错误");
            }
        }
        String pwd = DigestUtil.md5Hex(admin.getPassword());
        if(adminFid.getPassword().equals(pwd)){
            return ResponseUtils.errorRes("新密码不能与旧密码一致");
        }
        adminUPD.setId(adminFid.getId());
        adminUPD.setPassword(pwd);
        if (0 == adminMapper.updateByPrimaryKeySelective(adminUPD)) {
            throw new BusinessException("修改密码失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response adminHome(){
        AdminHomeETD adminHome = orderMapper.findAdminHome();
        return ResponseUtils.successRes(adminHome);
    }
}
