package com.tdpro.mapper;

import com.tdpro.entity.PAdvert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PAdvertMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAdvert record);

    int insertSelective(PAdvert record);

    PAdvert selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PAdvert record);

    int updateByPrimaryKey(PAdvert record);
}