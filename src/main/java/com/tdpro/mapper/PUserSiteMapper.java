package com.tdpro.mapper;

import com.tdpro.entity.PUserSite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserSiteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserSite record);

    int insertSelective(PUserSite record);

    PUserSite selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserSite record);

    int updateByPrimaryKey(PUserSite record);
}