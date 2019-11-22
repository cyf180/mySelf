package com.tdpro.mapper;

import com.tdpro.entity.PCollect;
import com.tdpro.entity.extend.CollectETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PCollectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PCollect record);

    int insertSelective(PCollect record);

    PCollect selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PCollect record);

    int updateByPrimaryKey(PCollect record);

    List<CollectETD> selectListByUid(Long uid);
}