package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.entity.PAdmin;
import com.tdpro.mapper.PAdminMapper;
import com.tdpro.service.AdminService;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private PAdminMapper adminMapper;
    @Override
    public PAdmin findByPhone(PAdmin admin) {
        if(StringUtil.isEmpty(admin.getPhone()) || StringUtil.isEmpty(admin.getPassword())){
            return null;
        }
        String pwd = DigestUtil.md5Hex(admin.getPassword());
        admin.setPassword(pwd);
        return adminMapper.findByPhoneAndPassword(admin);
    }

    @Override
    public PAdmin findById(Long id){
        return adminMapper.selectByPrimaryKey(id);
    }
}
