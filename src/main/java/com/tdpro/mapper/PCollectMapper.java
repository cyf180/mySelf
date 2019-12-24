package com.tdpro.mapper;

import com.tdpro.entity.PCollect;
import com.tdpro.entity.extend.CollectETD;
import com.tdpro.entity.extend.GoodsETD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PCollectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PCollect record);

    int insertSelective(PCollect record);

    PCollect selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PCollect record);

    int updateByPrimaryKey(PCollect record);

    List<GoodsETD> selectListByUid(Long uid);

    PCollect findByUidAndGoodsId(@Param("uid") Long uid,@Param("goodsId") Long goodsId);
}