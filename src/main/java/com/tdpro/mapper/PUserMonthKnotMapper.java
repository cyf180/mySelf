package com.tdpro.mapper;

import com.tdpro.entity.PUserMonthKnot;
import com.tdpro.entity.extend.UserMonthKnotPageETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PUserMonthKnotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserMonthKnot record);

    int insertSelective(PUserMonthKnot record);

    PUserMonthKnot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserMonthKnot record);

    int updateByPrimaryKey(PUserMonthKnot record);

    PUserMonthKnot findByUidAndYearAndMonth(PUserMonthKnot record);

    List<UserMonthKnotPageETD> findAdminPageList(UserMonthKnotPageETD knotPageETD);
}