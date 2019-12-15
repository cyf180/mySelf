package com.tdpro.mapper;

import com.tdpro.entity.PAdvert;
import com.tdpro.entity.extend.AdvertETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PAdvertMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAdvert record);

    int insertSelective(PAdvert record);

    PAdvert selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PAdvert record);

    int updateByPrimaryKey(PAdvert record);

    List<PAdvert> selectList();

    List<AdvertETD> findPageList(AdvertETD advertETD);

    AdvertETD findInfoById(Long id);
}