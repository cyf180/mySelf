package com.tdpro.mapper;

import com.tdpro.entity.PKnotConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PKnotConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PKnotConfig record);

    int insertSelective(PKnotConfig record);

    PKnotConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PKnotConfig record);

    int updateByPrimaryKey(PKnotConfig record);

    PKnotConfig findBySmallValueAndBigValueAndNotId(@Param("suitLevelNum") Integer suitLevelNum,@Param("id") Long id);

    List<PKnotConfig> findList();

    PKnotConfig findLtId(Long id);

    PKnotConfig findGtId(Long id);
}