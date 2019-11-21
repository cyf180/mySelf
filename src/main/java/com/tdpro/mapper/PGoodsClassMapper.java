package com.tdpro.mapper;

import com.tdpro.entity.PGoodsClass;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PGoodsClassMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsClass record);

    int insertSelective(PGoodsClass record);

    PGoodsClass selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsClass record);

    int updateByPrimaryKey(PGoodsClass record);
}