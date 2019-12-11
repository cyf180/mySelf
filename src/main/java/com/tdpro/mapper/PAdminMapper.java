package com.tdpro.mapper;

import com.tdpro.entity.PAdmin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PAdminMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAdmin record);

    int insertSelective(PAdmin record);

    PAdmin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PAdmin record);

    int updateByPrimaryKey(PAdmin record);

    PAdmin findByPhoneAndPassword(PAdmin admin);
}