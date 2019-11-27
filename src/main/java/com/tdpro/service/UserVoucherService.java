package com.tdpro.service;

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
    void updateUserVoucherIsUse(List<POrderVoucher> list);

    /**
     * 修改用户券为绑定
     * @param list
     */
    void updateUserVoucherIsLock(List<PUserVoucher> list);
}
