package com.tdpro.mapper;

import com.tdpro.entity.PCollect;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PCollectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PCollect record);

    int insertSelective(PCollect record);

    PCollect selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PCollect record);

    int updateByPrimaryKey(PCollect record);
}