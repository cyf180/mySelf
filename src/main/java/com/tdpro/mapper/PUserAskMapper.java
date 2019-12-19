package com.tdpro.mapper;

import com.tdpro.entity.PUserAsk;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserAskMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(PUserAsk record);

    int insertSelective(PUserAsk record);

    PUserAsk selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(PUserAsk record);

    int updateByPrimaryKeyWithBLOBs(PUserAsk record);
}