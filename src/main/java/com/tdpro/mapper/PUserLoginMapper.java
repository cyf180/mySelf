package com.tdpro.mapper;

import com.tdpro.entity.PUserLogin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserLoginMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserLogin record);

    int insertSelective(PUserLogin record);

    PUserLogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserLogin record);

    int updateByPrimaryKey(PUserLogin record);

    PUserLogin selectInfoByUid(Long uid);

    PUserLogin findByOpenId(PUserLogin userLogin);
}