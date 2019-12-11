package com.tdpro.service;




import com.tdpro.entity.PRole;
import com.tdpro.entity.extend.RoleETD;
import com.tdpro.service.Base.BaseService;

import java.util.List;

/**
 * @About
 * @Author jy
 * @Date 2018/5/31 15:16
 */
public interface RoleService extends BaseService<RoleETD> {
    List<RoleETD> selectAllRole();

    int editRole(RoleETD role);

    int changeStatus(Long id);
}
