package com.tdpro.service.impl;

import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.extend.OrderVoucherETD;
import com.tdpro.mapper.POrderVoucherMapper;
import com.tdpro.service.OrderVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderVoucherServiceImpl implements OrderVoucherService {
    @Autowired
    private POrderVoucherMapper orderVoucherMapper;
    @Override
    public Boolean insertVoucher(Long orderId, Long uid, Long voucherId) {
        POrderVoucher orderVoucherADD = new POrderVoucher();
        orderVoucherADD.setOrderId(orderId);
        orderVoucherADD.setUid(uid);
        orderVoucherADD.setUserVoucherId(voucherId);
        if(orderVoucherMapper.insertSelective(orderVoucherADD) == 0){
            return false;
        }
        return true;
    }

    @Override
    public List<POrderVoucher> findListByOrderId(Long orderId) {
        return orderVoucherMapper.findListByOrderId(orderId);
    }

    @Override
    public List<OrderVoucherETD> findAdminListByOrderId(Long orderId){
        List<OrderVoucherETD> voucherList = orderVoucherMapper.findAdminListByOrderId(orderId);
        return voucherList;
    }
}
