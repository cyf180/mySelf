package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
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
}
