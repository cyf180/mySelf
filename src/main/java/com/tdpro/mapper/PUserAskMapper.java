package com.tdpro.mapper;

import com.tdpro.entity.PUserAsk;
import com.tdpro.entity.extend.UserAskPageETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PUserAskMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(PUserAsk record);

    int insertSelective(PUserAsk record);

    PUserAsk selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(PUserAsk record);

    int updateByPrimaryKeyWithBLOBs(PUserAsk record);

    List<UserAskPageETD> findPageList(UserAskPageETD userAskPageETD);

    UserAskPageETD findInfo(Long uid);
}