package com.tdpro.mapper;

import com.tdpro.entity.PGoodsImg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PGoodsImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsImg record);

    int insertSelective(PGoodsImg record);

    PGoodsImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsImg record);

    int updateByPrimaryKey(PGoodsImg record);
}