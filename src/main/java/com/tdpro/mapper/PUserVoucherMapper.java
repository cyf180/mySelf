package com.tdpro.mapper;

import com.tdpro.entity.PUserVoucher;
import com.tdpro.entity.extend.UserVoucherETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PUserVoucherMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserVoucher record);

    int insertSelective(PUserVoucher record);

    PUserVoucher selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserVoucher record);

    int updateByPrimaryKey(PUserVoucher record);

    List<UserVoucherETD> selectListByUid(UserVoucherETD voucherETD);

    List<PUserVoucher> countByUidAndVoucherId(UserVoucherETD voucherETD);
}