package com.tdpro.mapper;

import com.tdpro.entity.PUserKnot;
import com.tdpro.entity.extend.UserKnotETD;
import com.tdpro.entity.extend.UserKnotPageETD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PUserKnotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserKnot record);

    int insertSelective(PUserKnot record);

    PUserKnot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserKnot record);

    int updateByPrimaryKey(PUserKnot record);

    int updateMonthId(@Param("idList") List<Long> idList,@Param("monthId") Long monthId);

    List<UserKnotETD> selectListByUid(UserKnotETD userKnotETD);

    List<UserKnotPageETD> findAdminPageList(UserKnotPageETD userKnotPageETD);
}