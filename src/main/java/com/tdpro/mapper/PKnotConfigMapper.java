package com.tdpro.mapper;

import com.tdpro.entity.PKnotConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PKnotConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PKnotConfig record);

    int insertSelective(PKnotConfig record);

    PKnotConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PKnotConfig record);

    int updateByPrimaryKey(PKnotConfig record);
}