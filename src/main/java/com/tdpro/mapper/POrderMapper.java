package com.tdpro.mapper;

import com.tdpro.entity.POrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface POrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(POrder record);

    int insertSelective(POrder record);

    POrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(POrder record);

    int updateByPrimaryKey(POrder record);
}