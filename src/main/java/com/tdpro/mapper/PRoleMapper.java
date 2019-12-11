package com.tdpro.mapper;

import com.tdpro.entity.PRole;
import com.tdpro.entity.extend.RoleETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PRole record);

    int insertSelective(RoleETD record);

    RoleETD selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleETD record);

    int updateByPrimaryKey(PRole record);

    List<RoleETD> selectPageList(RoleETD record);

    List<RoleETD> selectAllRole(RoleETD record);
}