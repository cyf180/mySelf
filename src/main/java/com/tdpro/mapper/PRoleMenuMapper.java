package com.tdpro.mapper;

import com.tdpro.entity.PRoleMenu;
import com.tdpro.entity.extend.RoleMenuETD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PRoleMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PRoleMenu record);

    int insertSelective(RoleMenuETD record);

    PRoleMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PRoleMenu record);

    int updateByPrimaryKey(PRoleMenu record);

    List<RoleMenuETD> selectList(RoleMenuETD record);

    int deleteWithMenu(Long mid);

    int deleteByRid(Long rid);

    RoleMenuETD selectByRidAndMid(@Param("rId") Long rId, @Param("mId") Long mId);
}