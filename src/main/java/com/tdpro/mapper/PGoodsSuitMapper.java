package com.tdpro.mapper;

import com.tdpro.entity.PGoodsSuit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PGoodsSuitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsSuit record);

    int insertSelective(PGoodsSuit record);

    PGoodsSuit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsSuit record);

    int updateByPrimaryKey(PGoodsSuit record);
}