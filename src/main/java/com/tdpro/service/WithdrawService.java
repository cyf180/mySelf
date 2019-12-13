package com.tdpro.service;


import com.tdpro.common.utils.Response;
import com.tdpro.entity.PWithdraw;
import com.tdpro.entity.extend.WithdrawETD;
import com.tdpro.entity.extend.WithdrawPageETD;

public interface WithdrawService {
    /**
     * 提现申请
     * @param withdraw
     * @return
     */
    Response withdrawApply(PWithdraw withdraw);

    /**
     * 提现记录列表
     * @param withdrawETD
     * @return
     */
    Response withdrawList(WithdrawETD withdrawETD);

    /**
     * 提现申请获取柜主详情
     * @param accountId
     * @return
     */
    Response userWithdrawInfo(Long accountId);

    /**
     * 提现列表
     *
     * @param record
     * @return
     */
    Response adminPageList(WithdrawPageETD record);

    /**
     * 修改提现状态
     *
     * @param record
     * @return
     */
    Response updateStatus(PWithdraw record,Long adminId);

    /**
     * 详情
     * @param id
     * @return
     */
    Response adminWithdrawInfo(Long id);
}
