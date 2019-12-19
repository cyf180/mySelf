package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.constant.IssueType;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.POrderVoucher;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserVoucher;
import com.tdpro.entity.PVoucher;
import com.tdpro.entity.extend.UserVoucherETD;
import com.tdpro.mapper.POrderVoucherMapper;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.mapper.PUserVoucherMapper;
import com.tdpro.mapper.PVoucherMapper;
import com.tdpro.service.UserVoucherService;
import com.tdpro.service.VoucherIssueLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserVoucherServiceImpl implements UserVoucherService {
    @Autowired
    private PUserVoucherMapper userVoucherMapper;
    @Autowired
    private PVoucherMapper voucherMapper;
    @Autowired
    private PUserMapper userMapper;
    @Autowired
    private VoucherIssueLogService issueLogService;
    @Autowired
    private POrderVoucherMapper orderVoucherMapper;

    @Override
    public Response userVoucherList(UserVoucherETD userVoucherETD) {
        Integer pageNo = userVoucherETD.getPageNo() == null ? 1 : userVoucherETD.getPageNo();
        Integer pageSize = userVoucherETD.getPageSize() == null ? 10: userVoucherETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<UserVoucherETD> voucherList = voucherMapper.selectListByUid(userVoucherETD);
        return ResponseUtils.successRes(new PageInfo(voucherList));
    }

    @Override
    public List<PUserVoucher> selectByUidAndVoucherId(Long uid, Long voucherId,int num) {
        UserVoucherETD voucherETD = new UserVoucherETD();
        voucherETD.setUid(uid);
        voucherETD.setVoucherId(voucherId);
        voucherETD.setNumber(num);
        List<PUserVoucher> voucherList = userVoucherMapper.countByUidAndVoucherId(voucherETD);
        return voucherList;
    }

    @Override
    public Boolean updateUserVoucherIsUse(List<PUserVoucher> list) {
        int update = userVoucherMapper.updateIsUse(list);
        if(update != list.size()){
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateUserVoucherIsUseBy(List<POrderVoucher> list) {
        int update = userVoucherMapper.updateIsUseByU(list);
        if(update != list.size()){
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateUserVoucherIsLock(List<PUserVoucher> list) {
        int update = userVoucherMapper.updateIsLock(list);
        if(update != list.size()){
            return false;
        }
        return true;
    }

    @Override
    public void voucherIssue(Long strawUid,Long payUid,IssueType issueType){
        PUser user = userMapper.selectByPrimaryKey(strawUid);
        if(null != user){
            PVoucher sixVoucher = voucherMapper.selectByPrimaryKey(1L);
            if(null != sixVoucher){
                if(!this.insertUserVoucher(sixVoucher,user,payUid,issueType)){
                    throw new BusinessException("添加优惠券失败");
                }
                if(user.getStrawUid().compareTo(new Long(0)) > 0){
                    PUser strawUser = userMapper.selectByPrimaryKey(user.getStrawUid());
                    if(null != strawUser){
                        PVoucher threeVoucher = voucherMapper.selectByPrimaryKey(2L);
                        if(null != threeVoucher) {
                            if (!this.insertUserVoucher(threeVoucher, strawUser, payUid, issueType)) {
                                throw new BusinessException("添加上级优惠券失败");
                            }
                        }else{
                            log.error("券发放失败,推荐人ID {},34元券异常",strawUid);
                        }
                    }else{
                        log.error("券发放失败,推荐人ID {},上级推荐人查询失败",strawUid);
                    }
                }
            }else{
                log.error("券发放失败,推荐人ID {},68元券异常",strawUid);
            }
        }else{
            log.error("券发放失败,推荐人ID {},查询失败",strawUid);
        }
    }

    @Override
    public Boolean releaseUserVoucher(Long orderId){
        List<POrderVoucher> voucherList = orderVoucherMapper.findListByOrderId(orderId);
        if(null != voucherList && voucherList.size() > 0){
            userVoucherMapper.updateRelease(voucherList);
        }
        return true;
    }
    @Override
    public boolean insertUserVoucher(PVoucher voucher,PUser user,Long payUid,IssueType issueType){
        PUserVoucher userVoucher = new PUserVoucher();
        userVoucher.setUid(user.getId());
        userVoucher.setVoucherId(voucher.getId());
        userVoucher.setState(0);
        userVoucher.setUseState(0);
        userVoucher.setCreateTime(new Date());
        int addUserVoucher = userVoucherMapper.insertSelective(userVoucher);
        boolean addIssue = issueLogService.insertIssueLog(user.getId(),payUid,voucher,issueType,userVoucher.getId());
        if(0 == addUserVoucher || !addIssue){
            log.error("优惠券发放失败,发放UID：{},添加用户优惠券状态：{},添加发放记录状态：{}",user.getId(),addUserVoucher,addIssue);
            return false;
        }
        return true;
    }
}
