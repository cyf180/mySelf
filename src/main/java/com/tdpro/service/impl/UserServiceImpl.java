package com.tdpro.service.impl;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.extend.UserETD;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PUserMapper userMapper;
    @Override
    public Response userInformation(Long uid) {
        UserETD userETD = userMapper.getUserCentre(uid);
        if(null == userETD){
            return ResponseUtils.errorRes("用户错误");
        }
        return ResponseUtils.successRes(userETD);
    }
}
