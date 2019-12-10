package com.tdpro.mapper;

import com.tdpro.entity.PUserMonthKnot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserMonthKnotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserMonthKnot record);

    int insertSelective(PUserMonthKnot record);

    PUserMonthKnot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserMonthKnot record);

    int updateByPrimaryKey(PUserMonthKnot record);

    PUserMonthKnot findByUidAndYearAndMonth(PUserMonthKnot record);
}