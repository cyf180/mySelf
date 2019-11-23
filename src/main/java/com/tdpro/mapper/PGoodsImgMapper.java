package com.tdpro.mapper;

import com.tdpro.entity.PGoodsImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PGoodsImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsImg record);

    int insertSelective(PGoodsImg record);

    PGoodsImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsImg record);

    int updateByPrimaryKey(PGoodsImg record);

    List<PGoodsImg> selectListByGoodsId(Long goodsId);
}