package com.tdpro.mapper;

import com.tdpro.entity.PUserVoucher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserVoucherMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserVoucher record);

    int insertSelective(PUserVoucher record);

    PUserVoucher selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserVoucher record);

    int updateByPrimaryKey(PUserVoucher record);
}