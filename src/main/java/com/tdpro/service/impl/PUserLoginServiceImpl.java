package com.tdpro.service.impl;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUserLogin;
import com.tdpro.mapper.PUserLoginMapper;
import com.tdpro.service.PUserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PUserLoginServiceImpl implements PUserLoginService {
    @Autowired
    private PUserLoginMapper userLoginMapper;
    @Override
    public Response findUserLogin(Long uid) {
        PUserLogin userLogin = userLoginMapper.selectInfoByUid(uid);
        if(null == userLogin){
            return ResponseUtils.errorRes("信息不存在");
        }
        return ResponseUtils.successRes(userLogin);
    }
}
