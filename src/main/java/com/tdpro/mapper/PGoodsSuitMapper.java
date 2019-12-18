package com.tdpro.mapper;

import com.tdpro.entity.PGoodsSuit;
import com.tdpro.entity.extend.GoodsSuitETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PGoodsSuitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PGoodsSuit record);

    int insertSelective(PGoodsSuit record);

    PGoodsSuit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PGoodsSuit record);

    int updateByPrimaryKey(PGoodsSuit record);

    List<GoodsSuitETD> selectAllListByGoodsId(Long goodsId);

    List<PGoodsSuit> selectAdminListByGoodsId(Long goodsId);

    int countByGoodsId(Long goodsId);

    int deleteByGoodsId(Long goodsId);
}