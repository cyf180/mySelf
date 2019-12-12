package com.tdpro.mapper;

import com.tdpro.entity.PUserPayConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserPayConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserPayConfig record);

    int insertSelective(PUserPayConfig record);

    PUserPayConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserPayConfig record);

    int updateByPrimaryKey(PUserPayConfig record);

    PUserPayConfig findByType(PUserPayConfig payConfig);
}