package com.tdpro.mapper;

import com.tdpro.entity.PCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PCartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PCart record);

    int insertSelective(PCart record);

    PCart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PCart record);

    int updateByPrimaryKey(PCart record);
}