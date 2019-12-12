package com.tdpro.mapper;

import com.tdpro.entity.PUserPay;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PUserPayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUserPay record);

    int insertSelective(PUserPay record);

    PUserPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUserPay record);

    int updateByPrimaryKey(PUserPay record);

    PUserPay findByPayNo(PUserPay userPay);
}