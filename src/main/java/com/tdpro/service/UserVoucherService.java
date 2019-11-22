package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.UserVoucherETD;

public interface UserVoucherService {
    /**
     * 用户优惠券列表
     * @param userVoucherETD
     * @return
     */
    Response userVoucherList(UserVoucherETD userVoucherETD);
}
