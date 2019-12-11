package com.tdpro.mapper;

import com.tdpro.entity.PMenu;
import com.tdpro.entity.extend.MenuETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PMenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PMenu record);

    int insertSelective(MenuETD record);

    MenuETD selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MenuETD record);

    int updateByPrimaryKey(MenuETD record);

    List<MenuETD> selectListByRid(Map param);

    List<MenuETD> selectList(Map<String, String> param);

    int getCount(MenuETD menu);

    PMenu selectByRid(Map param);
}