package com.tdpro.service;

import com.tdpro.common.constant.IssueType;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.VoucherPageETD;

public interface VoucherService {
    Response adminPageList(VoucherPageETD voucherPageETD);
}
