package com.tdpro.mapper;

import com.tdpro.entity.PGoodsExchange;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PGoodsExchangeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsExchange record);

    int insertSelective(PGoodsExchange record);

    PGoodsExchange selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsExchange record);

    int updateByPrimaryKey(PGoodsExchange record);
}