package com.tdpro.mapper;

import com.tdpro.entity.PVoucherIssueLog;
import com.tdpro.entity.extend.VoucherIssueETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PVoucherIssueLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PVoucherIssueLog record);

    int insertSelective(PVoucherIssueLog record);

    PVoucherIssueLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PVoucherIssueLog record);

    int updateByPrimaryKey(PVoucherIssueLog record);

    List<VoucherIssueETD> selectListByUid(Long id);
}