package com.tdpro.mapper;

import com.tdpro.entity.PVoucher;
import com.tdpro.entity.extend.UserVoucherETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PVoucherMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PVoucher record);

    int insertSelective(PVoucher record);

    PVoucher selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PVoucher record);

    int updateByPrimaryKey(PVoucher record);

    List<UserVoucherETD> selectListByUid(UserVoucherETD voucherETD);
}