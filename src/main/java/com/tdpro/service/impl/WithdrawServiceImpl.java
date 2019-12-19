package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PDealLog;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserPayConfig;
import com.tdpro.entity.PWithdraw;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.PWithdrawMapper;
import com.tdpro.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WithdrawServiceImpl implements WithdrawService {
    @Autowired
    private UserService userService;
    @Autowired
    private PWithdrawMapper withdrawMapper;
    @Autowired
    private UserPayConfigService sysConfigMapper;
    @Autowired
    private DealLogService dealLogService;
    @Autowired
    private LogService logService;

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response withdrawApply(PWithdraw applyVo) {
        if (null == applyVo.getAmount() || applyVo.getAmount().compareTo(new BigDecimal("0")) <= 0) {
            return ResponseUtils.errorRes("提现金额有误");
        }
        PUser userInfo = userService.findById(applyVo.getUid());
        if (null == userInfo) {
            return ResponseUtils.errorRes("用户不存在");
        }
        if(userInfo.getIsUser().equals(new Integer(0))){
            return ResponseUtils.errorRes("非会员不能体现");
        }
        if(StringUtil.isEmpty(userInfo.getBankCard()) || StringUtil.isEmpty(userInfo.getBankName()) || StringUtil.isEmpty(userInfo.getBankBranch()) || StringUtil.isEmpty(userInfo.getName())){
            return ResponseUtils.errorRes("请先完善个人信息");
        }
        PUserPayConfig config = sysConfigMapper.findByType(1);
        if (null == config) {
            return ResponseUtils.errorRes("未开启提现功能");
        }
        //后台配置利率
        BigDecimal rate = config.getPrice();
        //用户申请提现金额
        BigDecimal applyAmount = applyVo.getAmount().setScale(2, BigDecimal.ROUND_DOWN);
        //用户新余额
        BigDecimal newBalance = userInfo.getBalance().subtract(applyAmount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseUtils.errorRes("提现余额不足");
        }
        //手续费
        BigDecimal poundage = applyAmount.multiply(rate).setScale(2, BigDecimal.ROUND_DOWN);
        //实际到账金额
        BigDecimal arrivalAmount = applyAmount.subtract(poundage);
        if(applyAmount.compareTo(new BigDecimal("0"))<0){
            return ResponseUtils.errorRes("提现利率有误");
        }
        //添加提现记录
        PWithdraw insetWithdraw = insertWithdraw(userInfo, config, applyAmount, newBalance, poundage, arrivalAmount);
        if (null == insetWithdraw) {
            log.error("提现申请添加提现记录失败,用户ID：" + userInfo.getId());
            throw new RuntimeException("添加提现失败");
        }
        //修改柜主余额
        UserBalanceUpdateETD userUPD = new UserBalanceUpdateETD();
        userUPD.setId(userInfo.getId());
        userUPD.setBalance(newBalance);
        userUPD.setOldBalance(userInfo.getBalance());
        boolean updateCabinetOwner = userService.updateUserBalance(userUPD);
        if (!updateCabinetOwner) {
            log.error("提现申请失败,修改柜主提现余额状态：" + updateCabinetOwner + "用户ID：" + userInfo.getId());
            throw new RuntimeException("提现申请失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response withdrawList(WithdrawETD withdrawPage) {
        Integer pageNo = withdrawPage.getPageNo() == null ? 1 : withdrawPage.getPageNo();
        Integer pageSize = withdrawPage.getPageSize() == null ? 10 : withdrawPage.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<WithdrawETD> withdrawPageList = withdrawMapper.getUserList(withdrawPage);
        PageInfo pageInfo = new PageInfo(withdrawPageList);
        return ResponseUtils.successRes(pageInfo);
    }

    @Override
    public Response userWithdrawInfo(Long uid) {
        UserWithdrawETD ownerInfo = new UserWithdrawETD();
        PUser userInfo = userService.findById(uid);
        if (null == userInfo) {
            return ResponseUtils.errorRes("用户不存在");
        }
        PUserPayConfig config = sysConfigMapper.findByType(1);
        if (null == config) {
            return ResponseUtils.errorRes("未开启提现功能");
        }
        ownerInfo.setBankName(userInfo.getBankName());
        ownerInfo.setBankBranch(userInfo.getBankBranch());
        ownerInfo.setBankCard(userInfo.getBankCard());
        ownerInfo.setName(userInfo.getName());
        ownerInfo.setBalance(userInfo.getBalance());
        ownerInfo.setRate(config.getPrice());
        return ResponseUtils.successRes(ownerInfo);
    }

    @Override
    public Response adminPageList(WithdrawPageETD record) {
        Integer pageNum = record.getPageNo() == null ? 1 : record.getPageNo();
        Integer pageSize = record.getPageSize() == null ? 10 : record.getPageSize();
        WithdrawDO withdrawDO = new WithdrawDO();
        withdrawDO = withdrawMapper.sumWithdraw(record);
        PageHelper.startPage(pageNum, pageSize);
        List<WithdrawPageETD> list = withdrawMapper.selectPageList(record);
        PageInfo pageInfo = new PageInfo(list);
        withdrawDO.setPageInfo(pageInfo);
        withdrawDO.setQueryModel(record);
        return ResponseUtils.successRes(withdrawDO);
    }

    @Override
    public Response updateStatus(PWithdraw record,Long adminId) {
        if (null == record.getId()) {
            return ResponseUtils.errorRes("关键值错误");
        }
        if (null != record.getStatus()) {
            if (!record.getStatus().equals(new Integer(-1)) && !record.getStatus().equals(new Integer(1))) {
                return ResponseUtils.errorRes("提现状态错误");
            }
        }
        PWithdraw withdrawVo = withdrawMapper.selectByPrimaryKey(record.getId());
        if (null == withdrawVo) {
            return ResponseUtils.errorRes("提现记录不存在");
        }
        if (withdrawVo.getAmount().compareTo(new BigDecimal("0")) <= 0 || withdrawVo.getArrivalAmount().compareTo(new BigDecimal("0")) <= 0) {
            return ResponseUtils.errorRes("当前记录提现金额有误");
        }
        StringBuffer note = new StringBuffer();
        PWithdraw withdrawUPD = new PWithdraw();
        withdrawUPD.setId(record.getId());
        if (null != record.getStatus() && !withdrawVo.getStatus().equals(record.getStatus())) {
            withdrawUPD.setStatus(record.getStatus());
            withdrawUPD.setAuditTime(new Date());
            note.append("提现状态由: [ ").append(withdrawVo.getStatus()).append(" ]修改为 [ ").append(record.getStatus()).append(" ],");
        }
        if (!StringUtils.isEmpty(record.getNote())) {
            withdrawUPD.setNote(record.getNote());
            note.append("备注：").append(record.getNote());
        }
        if (null != withdrawUPD.getStatus() || !StringUtils.isEmpty(withdrawUPD.getNote())) {
            logService.insertLog(adminId, "后台提现审核", note.toString(),1);
            if (0 == withdrawMapper.updateByPrimaryKeySelective(withdrawUPD)) {
                throw new BusinessException("提现操作失败");
            }
            //提现失败退回柜主提现金额
            Response updateOwner = updateOwner(withdrawVo, withdrawUPD);
            if (updateOwner != null) return updateOwner;
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response adminWithdrawInfo(Long id) {
        PWithdraw withdrawVo = withdrawMapper.selectByPrimaryKey(id);
        if (null == withdrawVo) {
            return ResponseUtils.errorRes("提现记录不存在");
        }
        return ResponseUtils.successRes(withdrawVo);
    }

    /**
     * 用户提现金额修改
     *
     * @param withdrawVo
     * @param withdrawUPD
     * @return
     */
    private Response updateOwner(PWithdraw withdrawVo, PWithdraw withdrawUPD) {
        if (null == withdrawUPD.getStatus()) {
            return null;
        }
        PUser cabinetOwner = userService.findById(withdrawVo.getUid());
        switch (withdrawUPD.getStatus().intValue()) {
            case -1:
                BigDecimal applyAmount = withdrawVo.getAmount();
                if (null == cabinetOwner) {
                    return ResponseUtils.errorRes("用户不存在");
                }
                if (cabinetOwner.getBalance().compareTo(new BigDecimal("0")) < 0) {
                    return ResponseUtils.errorRes("用户提现金额有误");
                }
                UserBalanceUpdateETD ownerUPD = new UserBalanceUpdateETD();
                ownerUPD.setId(cabinetOwner.getId());
                ownerUPD.setOldBalance(cabinetOwner.getBalance());
                ownerUPD.setBalance(cabinetOwner.getBalance().add(applyAmount));
                boolean update = userService.updateUserBalance(ownerUPD);
                if (!update) {
                    throw new BusinessException("修改柜主提现金额失败");
                }
                break;
            case 1:
                if (!dealLogService.insertDealLog("用户提现",null,withdrawVo.getAmount(),cabinetOwner.getBalance(),withdrawVo.getUid(),null)) {
                    throw new BusinessException("添加支出记录失败");
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 添加提现记录
     *
     * @param user
     * @param config
     * @param applyAmount
     * @param newBalance
     * @param poundage
     * @param arrivalAmount
     * @return
     */
    private PWithdraw insertWithdraw(PUser user, PUserPayConfig config, BigDecimal applyAmount, BigDecimal newBalance, BigDecimal poundage, BigDecimal arrivalAmount) {
        PWithdraw withdrawADD = new PWithdraw();
        withdrawADD.setStatus(0);
        withdrawADD.setUid(user.getId());
        withdrawADD.setAmount(applyAmount);
        withdrawADD.setArrivalAmount(arrivalAmount.abs());
        withdrawADD.setPoundage(poundage);
        withdrawADD.setBalance(newBalance);
        withdrawADD.setRate(config.getPrice());
        withdrawADD.setApplyTime(new Date());
        withdrawADD.setName(user.getName());
        withdrawADD.setIdCard(user.getIdCard());
        withdrawADD.setBankName(user.getBankName());
        withdrawADD.setBankBranch(user.getBankBranch());
        withdrawADD.setBankCard(user.getBankCard());
        if (withdrawMapper.insertSelective(withdrawADD) == 0) {
            return null;
        }
        return withdrawADD;
    }
}
