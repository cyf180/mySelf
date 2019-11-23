package com.tdpro.mapper;

import com.tdpro.entity.PAdvert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PAdvertMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAdvert record);

    int insertSelective(PAdvert record);

    PAdvert selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PAdvert record);

    int updateByPrimaryKey(PAdvert record);

    List<PAdvert> selectList();
}