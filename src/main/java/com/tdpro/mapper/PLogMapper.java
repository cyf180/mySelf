package com.tdpro.mapper;

import com.tdpro.entity.PLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PLog record);

    int insertSelective(PLog record);

    PLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PLog record);

    int updateByPrimaryKey(PLog record);
}