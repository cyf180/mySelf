package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.constant.IssueType;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PVoucher;
import com.tdpro.entity.PVoucherIssueLog;
import com.tdpro.entity.extend.UserSiteETD;
import com.tdpro.entity.extend.VoucherIssueETD;
import com.tdpro.mapper.PVoucherIssueLogMapper;
import com.tdpro.service.VoucherIssueLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VoucherIssueLogServiceImpl implements VoucherIssueLogService {
    @Autowired
    private PVoucherIssueLogMapper issueLogMapper;
    @Override
    public Boolean insertIssueLog(Long uid, Long payUid, PVoucher voucher, IssueType issueType,Long userVoucherId){
        PVoucherIssueLog issueLogADD = new PVoucherIssueLog();
        issueLogADD.setUid(uid);
        if(null != payUid){
            issueLogADD.setPayUid(payUid);
        }
        issueLogADD.setVoucherId(voucher.getId());
        issueLogADD.setVoucherName(voucher.getVoucherName());
        issueLogADD.setType(issueType.getType());
        issueLogADD.setCreateTime(new Date());
        issueLogADD.setUserVoucherId(userVoucherId);
        if(0 == issueLogMapper.insertSelective(issueLogADD)){
            return false;
        }
        return true;
    }

    @Override
    public Response userIssueList(VoucherIssueETD voucherIssueETD){
        Integer pageNo = voucherIssueETD.getPageNo() == null ? 1 : voucherIssueETD.getPageNo();
        Integer pageSize = voucherIssueETD.getPageSize() == null ? 10: voucherIssueETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<VoucherIssueETD> siteList = issueLogMapper.selectListByUid(voucherIssueETD.getUid());
        PageInfo pageInfo = new PageInfo(siteList);
        return ResponseUtils.successRes(pageInfo);
    }
}
