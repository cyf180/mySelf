package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PUserVoucher;
import com.tdpro.entity.extend.UserVoucherETD;
import com.tdpro.mapper.PUserVoucherMapper;
import com.tdpro.mapper.PVoucherMapper;
import com.tdpro.service.UserVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserVoucherServiceImpl implements UserVoucherService {
    @Autowired
    private PUserVoucherMapper userVoucherMapper;
    @Autowired
    private PVoucherMapper voucherMapper;

    @Override
    public Response userVoucherList(UserVoucherETD userVoucherETD) {
        Integer pageNo = userVoucherETD.getPageNo() == null ? 1 : userVoucherETD.getPageNo();
        Integer pageSize = userVoucherETD.getPageSize() == null ? 10: userVoucherETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<UserVoucherETD> voucherList = voucherMapper.selectListByUid(userVoucherETD);
        return ResponseUtils.successRes(new PageInfo(voucherList));
    }

    @Override
    public List<PUserVoucher> selectByUidAndVoucherId(Long uid, Long voucherId,int num) {
        UserVoucherETD voucherETD = new UserVoucherETD();
        voucherETD.setUid(uid);
        voucherETD.setVoucherId(voucherId);
        voucherETD.setNumber(num);
        List<PUserVoucher> voucherList = userVoucherMapper.countByUidAndVoucherId(voucherETD);
        return voucherList;
    }

    @Override
    public void updateUserVoucherIsUse(List<POrderVoucher> list) {
        int update = userVoucherMapper.updateIsUse(list);
        if(update != list.size()){
            throw new RuntimeException("兑换券处理异常");
        }
    }

    @Override
    public void updateUserVoucherIsLock(List<PUserVoucher> list) {
        int update = userVoucherMapper.updateIsLock(list);
        if(update != list.size()){
            throw new RuntimeException("兑换券处理异常");
        }
    }
}
