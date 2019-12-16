package com.tdpro.mapper;

import com.tdpro.entity.PAdmin;
import com.tdpro.entity.extend.AdminPageETD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PAdminMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PAdmin record);

    int insertSelective(PAdmin record);

    PAdmin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PAdmin record);

    int updateByPrimaryKey(PAdmin record);

    PAdmin findByPhoneAndPassword(PAdmin admin);

    List<AdminPageETD> findPageList(AdminPageETD adminPageETD);

    PAdmin findByPhone(@Param("phone") String phone);

    PAdmin findByPhoneAndNoId(@Param("phone") String phone,@Param("id") Long id);
}