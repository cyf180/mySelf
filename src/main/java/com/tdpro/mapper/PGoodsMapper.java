package com.tdpro.mapper;

import com.tdpro.entity.PGoods;
import com.tdpro.entity.extend.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoods record);

    int insertSelective(PGoods record);

    PGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoods record);

    int updateByPrimaryKey(PGoods record);

    List<GoodsETD> pageList(GoodsETD goodsETD);

    GoodsETD selectInfo(GoodsETD goodsETD);

    int updateRepertory(GoodsRepertoryUpdateETD repertoryUpdateETD);

    List<GoodsPageETD> adminPageList(GoodsPageETD goodsPageETD);

    GoodsInfoETD findAdminById(Long id);

    PGoods findByNameAndZoneType(@Param("goodsName") String goodsName,@Param("zoneType") Integer zoneType);
}