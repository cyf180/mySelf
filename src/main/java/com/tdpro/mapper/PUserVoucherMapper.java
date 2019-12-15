package com.tdpro.mapper;

import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PUserVoucher;
import com.tdpro.entity.extend.UserVoucherETD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    int updateIsUse(@Param("list") List<POrderVoucher> list);

    int updateIsLock(@Param("list") List<PUserVoucher> list);

    int updateRelease(@Param("list") List<POrderVoucher> list);
}