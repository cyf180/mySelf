package com.tdpro.mapper;

import com.tdpro.entity.PGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoods record);

    int insertSelective(PGoods record);

    PGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoods record);

    int updateByPrimaryKey(PGoods record);
}