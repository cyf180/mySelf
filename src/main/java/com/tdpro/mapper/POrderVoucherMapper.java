package com.tdpro.mapper;

import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.extend.OrderVoucherETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface POrderVoucherMapper {
    int deleteByPrimaryKey(Long id);

    int insert(POrderVoucher record);

    int insertSelective(POrderVoucher record);

    POrderVoucher selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(POrderVoucher record);

    int updateByPrimaryKey(POrderVoucher record);

    List<POrderVoucher> findListByOrderId(Long orderId);

    List<OrderVoucherETD> findAdminListByOrderId(Long orderId);
}