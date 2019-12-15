package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.constant.IssueType;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserVoucher;
import com.tdpro.entity.PVoucher;
import com.tdpro.entity.extend.AdvertETD;
import com.tdpro.entity.extend.VoucherPageETD;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.mapper.PUserVoucherMapper;
import com.tdpro.mapper.PVoucherMapper;
import com.tdpro.service.VoucherIssueLogService;
import com.tdpro.service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private PVoucherMapper voucherMapper;

    @Override
    public Response adminPageList(VoucherPageETD voucherPageETD){
        Integer pageNum = voucherPageETD.getPageNo() == null ? 1 : voucherPageETD.getPageNo();
        Integer pageSize = voucherPageETD.getPageSize() == null ? 10 : voucherPageETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<VoucherPageETD> list = voucherMapper.findPageList(voucherPageETD);
        Map map = new HashMap();
        map.put("pageInfo",new PageInfo(list));
        map.put("queryModel",voucherPageETD);
        return ResponseUtils.successRes(map);
    }
}
