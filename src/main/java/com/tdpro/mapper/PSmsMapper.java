package com.tdpro.mapper;

import com.tdpro.entity.PSms;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PSmsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PSms record);

    int insertSelective(PSms record);

    PSms selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PSms record);

    int updateByPrimaryKey(PSms record);

    PSms findOne();
}