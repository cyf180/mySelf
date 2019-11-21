package com.tdpro.mapper;

import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.UserETD;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUser record);

    int insertSelective(PUser record);

    PUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUser record);

    int updateByPrimaryKey(PUser record);

    UserETD getUserCentre(Long id);

    int getTeamPeopleNum(Long id);
}