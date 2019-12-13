package com.tdpro.mapper;

import com.tdpro.entity.PGoodsClass;
import com.tdpro.entity.extend.GoodsClassPageETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PGoodsClassMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsClass record);

    int insertSelective(PGoodsClass record);

    PGoodsClass selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsClass record);

    int updateByPrimaryKey(PGoodsClass record);

    List<PGoodsClass> selectList(Integer num);

    List<GoodsClassPageETD> findGoodsClassPageList(GoodsClassPageETD classPageETD);
}