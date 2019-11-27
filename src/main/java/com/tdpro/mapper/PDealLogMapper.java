package com.tdpro.mapper;

import com.tdpro.entity.PDealLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PDealLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PDealLog record);

    int insertSelective(PDealLog record);

    PDealLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PDealLog record);

    int updateByPrimaryKey(PDealLog record);
}