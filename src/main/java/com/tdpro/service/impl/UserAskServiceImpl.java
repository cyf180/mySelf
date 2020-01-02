package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.common.constant.IssueType;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserAsk;
import com.tdpro.entity.PVoucher;
import com.tdpro.mapper.PUserAskMapper;
import com.tdpro.mapper.PVoucherMapper;
import com.tdpro.service.UserAskService;
import com.tdpro.service.UserService;
import com.tdpro.service.UserVoucherService;
import com.tdpro.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAskServiceImpl implements UserAskService {
    @Autowired
    private PUserAskMapper userAskMapper;
    @Autowired
    private UserVoucherService userVoucherService;
    @Autowired
    private UserService userService;
    @Autowired
    private PVoucherMapper voucherMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addUserAsk(PUserAsk userAsk) {
        if(StringUtil.isEmpty(userAsk.getContent())){
            return ResponseUtils.errorRes("问卷内容错误");
        }
        PUserAsk userAskInfo = userAskMapper.selectByPrimaryKey(userAsk.getUid());
        if(null == userAskInfo){
            PUser user = userService.findById(userAsk.getUid());
            if(null == user){
                return ResponseUtils.errorRes("用户异常");
            }
            PVoucher sixVoucher = voucherMapper.selectByPrimaryKey(1L);
            if(null == sixVoucher){
                return ResponseUtils.errorRes("券异常");
            }
            PUserAsk userAskADD = new PUserAsk();
            userAskADD.setUid(userAsk.getUid());
            userAskADD.setContent(userAsk.getContent());
            if(0 == userAskMapper.insertSelective(userAskADD)){
                throw new BusinessException("保存问卷失败");
            }
            boolean issue = userVoucherService.insertUserVoucher(sixVoucher,user,null,IssueType.USER_ASK_TYPE);
            if(!issue){
                throw new BusinessException("获取券失败");
            }
            userVoucherService.voucherIssue(user.getStrawUid(),user.getId(),IssueType.USER_ASK_TYPE);

        }
        return ResponseUtils.successRes(1);
    }
}
