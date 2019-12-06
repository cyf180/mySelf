package com.tdpro.mapper;

import com.tdpro.entity.PSmsCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PSmsCodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PSmsCode record);

    int insertSelective(PSmsCode record);

    PSmsCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PSmsCode record);

    int updateByPrimaryKey(PSmsCode record);

    PSmsCode findByPhoneAndCode(PSmsCode record);
}