package com.tdpro.mapper;

import com.tdpro.entity.PGoodsExchange;
import com.tdpro.entity.extend.GoodsExchangeETD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PGoodsExchangeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsExchange record);

    int insertSelective(PGoodsExchange record);

    PGoodsExchange selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsExchange record);

    int updateByPrimaryKey(PGoodsExchange record);

    List<GoodsExchangeETD> selectListByGoodsId(Long goodsId);

    List<GoodsExchangeETD> selectListByGoodsIdAndUid(@Param("goodsId") Long goodsId,@Param("uid") Long uid);

    GoodsExchangeETD selectByGoodsIdAndVoucherId(@Param("goodsId") Long goodsId, @Param("voucherId") Long voucherId);

    int deleteByGoodsId(Long goodsId);
}