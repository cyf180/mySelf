package com.tdpro.service;

import com.tdpro.common.constant.IssueType;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PUserVoucher;
import com.tdpro.entity.extend.UserVoucherETD;

import java.util.List;

public interface UserVoucherService {
    /**
     * 用户优惠券列表
     * @param userVoucherETD
     * @return
     */
    Response userVoucherList(UserVoucherETD userVoucherETD);

    /**
     * 查询用户券列表
     * @param uid
     * @param voucherId
     * @return
     */
    List<PUserVoucher> selectByUidAndVoucherId(Long uid,Long voucherId,int num);

    /**
     * 修改用户优惠券未已使用
     * @param list
     * @return
     */
    Boolean updateUserVoucherIsUse(List<PUserVoucher> list);

    Boolean updateUserVoucherIsUseBy(List<POrderVoucher> list);

    /**
     * 修改用户券为绑定
     * @param list
     */
    Boolean updateUserVoucherIsLock(List<PUserVoucher> list);

    void voucherIssue(Long strawUid,Long payUid,IssueType issueType);

    Boolean releaseUserVoucher(Long orderId);
}
