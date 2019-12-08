package com.tdpro.mapper;

import com.tdpro.entity.PUserMonthKnot;

public interface PUserMonthKnotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserMonthKnot record);

    int insertSelective(PUserMonthKnot record);

    PUserMonthKnot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserMonthKnot record);

    int updateByPrimaryKey(PUserMonthKnot record);

    PUserMonthKnot findByUidAndYearAndMonth(PUserMonthKnot record);
}