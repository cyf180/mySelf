package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.entity.PRole;
import com.tdpro.entity.extend.RoleETD;
import com.tdpro.entity.extend.RoleMenuETD;
import com.tdpro.mapper.PRoleMapper;
import com.tdpro.mapper.PRoleMenuMapper;
import com.tdpro.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @About
 * @Author jy
 * @Date 2018/5/31 15:17
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private PRoleMenuMapper roleMenuMapper;
    @Autowired
    private PRoleMapper roleMapper;

    @Override
    public List<RoleETD> selectAllRole() {
        return roleMapper.selectAllRole(new RoleETD());
    }

    @Override
    public int editRole(RoleETD role) {
        int insertRole = 0;
        RoleMenuETD privilege = new RoleMenuETD();
        if (role.getInsertOrUpdate().equals(1)) {
            role.setId(null);
            // 新增角色
            role.setRoleStatus(0);
            insertRole = insertSelective(role);
        } else {
            // 修改角色
            insertRole = updateByPrimaryKey(role);
            // 删除角色的所有权限
            roleMenuMapper.deleteByRid(role.getId());
        }

        // 赋予权限
        if (role.getPrivileges() == null || role.getPrivileges().trim().equals("")) {
            return 1;
        }
        String[] pArr = role.getPrivileges().split(",");
        for (int i = 0; i < pArr.length; i++) {
            privilege.clear();
            privilege.setMid(Integer.valueOf(pArr[i]));
            privilege.setRid(role.getId().intValue());
            roleMenuMapper.insertSelective(privilege);
        }
        return 1;
    }

    @Override
    public int changeStatus(Long id) {
        int result = -1;
        RoleETD role = roleMapper.selectByPrimaryKey(id);
        if(null!=role) {
            Integer status = role.getRoleStatus();
            switch (status) {
                case 0:
                    role.setRoleStatus(1);
                    break;
                case 1:
                    role.setRoleStatus(0);
                    break;
                default:
                    break;
            }
            result = roleMapper.updateByPrimaryKeySelective(role);
        }
        return result;
    }

    @Override
    public int insert(RoleETD record) {
        return 0;
    }

    @Override
    public int insertSelective(RoleETD record) {
        return roleMapper.insertSelective(record);
    }

    @Override
    public RoleETD selectByPrimaryKey(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<RoleETD> selectList(RoleETD record) {
        return null;
    }

    @Override
    public PageInfo selectPageList(RoleETD record) {
        Integer pageNum = record.getPageNo() == null ? 1 : record.getPageNo();
        Integer pageSize = record.getPageSize() == null ? 10 : record.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<RoleETD> list = roleMapper.selectPageList(record);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public int updateByPrimaryKeySelective(RoleETD record) {
        return roleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RoleETD record) {
        return roleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Long id, Long uId, String ip) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deletes(Integer[] ids) {
        return 0;
    }

}
