package com.tdpro.mapper;

import com.tdpro.entity.PUserSite;
import com.tdpro.entity.extend.UserSiteETD;
import com.tdpro.entity.extend.UserSitePageETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PUserSiteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserSite record);

    int insertSelective(PUserSite record);

    PUserSite selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserSite record);

    int updateByPrimaryKey(PUserSite record);

    List<UserSiteETD> selectListByUid(Long uid);

    int updateNotIsDefaultByUid(UserSiteETD siteETD);

    UserSiteETD selectOneByUid(Long uid);

    List<UserSitePageETD> selectPageList(UserSitePageETD sitePageETD);
}