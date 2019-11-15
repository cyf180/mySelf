package com.zerocat.mapper;

import com.zerocat.entity.DUserLogin;

public interface DUserLoginMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DUserLogin record);

    int insertSelective(DUserLogin record);

    DUserLogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DUserLogin record);

    int updateByPrimaryKey(DUserLogin record);
}