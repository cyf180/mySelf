package com.tdpro.mapper;

import com.tdpro.entity.PCart;
import com.tdpro.entity.extend.CartETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PCartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PCart record);

    int insertSelective(PCart record);

    PCart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PCart record);

    int updateByPrimaryKey(PCart record);

    List<CartETD> findListByOrderId(Long orderId);
}