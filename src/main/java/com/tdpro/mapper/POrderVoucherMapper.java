package com.tdpro.mapper;

import com.tdpro.entity.POrderVoucher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface POrderVoucherMapper {
    int deleteByPrimaryKey(Long id);

    int insert(POrderVoucher record);

    int insertSelective(POrderVoucher record);

    POrderVoucher selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(POrderVoucher record);

    int updateByPrimaryKey(POrderVoucher record);
}