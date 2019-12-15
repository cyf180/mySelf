package com.tdpro.service;

import com.tdpro.common.constant.IssueType;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.PVoucher;
import com.tdpro.entity.PVoucherIssueLog;
import com.tdpro.entity.extend.VoucherIssueETD;

public interface VoucherIssueLogService {
    Boolean insertIssueLog(Long uid, Long payUid, PVoucher voucher, IssueType issueType,Long userVoucherId);

    Response userIssueList(VoucherIssueETD voucherIssueETD);
}
