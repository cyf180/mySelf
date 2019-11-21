package com.tdpro.mapper;

import com.tdpro.entity.PUserKnot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserKnotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserKnot record);

    int insertSelective(PUserKnot record);

    PUserKnot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserKnot record);

    int updateByPrimaryKey(PUserKnot record);
}