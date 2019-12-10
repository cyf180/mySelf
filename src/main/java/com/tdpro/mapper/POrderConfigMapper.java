package com.tdpro.mapper;

import com.tdpro.entity.POrderConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface POrderConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(POrderConfig record);

    int insertSelective(POrderConfig record);

    POrderConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(POrderConfig record);

    int updateByPrimaryKey(POrderConfig record);

    POrderConfig findByType(@Param("type") Integer type);
}