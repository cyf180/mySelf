package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUserSite;
import com.tdpro.entity.extend.UserSiteETD;
import com.tdpro.entity.extend.UserSitePageETD;
import com.tdpro.mapper.PUserSiteMapper;
import com.tdpro.service.LogService;
import com.tdpro.service.UserSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserSiteServiceImpl implements UserSiteService {
    @Autowired
    private PUserSiteMapper userSiteMapper;
    @Autowired
    private LogService logService;

    @Override
    public Response userSiteList(UserSiteETD siteETD) {
        Integer pageNo = siteETD.getPageNo() == null ? 1 : siteETD.getPageNo();
        Integer pageSize = siteETD.getPageSize() == null ? 10: siteETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<UserSiteETD> siteList = userSiteMapper.selectListByUid(siteETD.getUid());
        PageInfo pageInfo = new PageInfo(siteList);
        return ResponseUtils.successRes(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response delUserSite(UserSiteETD siteETD) {
        if(null == siteETD.getId() || null == siteETD.getUid()){
            return  ResponseUtils.errorRes("操作异常");
        }
        PUserSite siteInfo = userSiteMapper.selectByPrimaryKey(siteETD.getId());
        if(null == siteInfo){
            return ResponseUtils.errorRes("收货地址不存在");
        }
        if(!siteInfo.getUid().equals(siteETD.getUid())){
            return ResponseUtils.errorRes("操作异常");
        }
        logService.insertLog(siteETD.getUid(),"会员删除收货地址","会员删除收货地址ID: "+siteInfo.getId(),0);
        if(0 == userSiteMapper.deleteByPrimaryKey(siteInfo.getId())){
            throw new RuntimeException("删除失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public  Response setUserSiteDefault(UserSiteETD siteETD){
        if(null == siteETD.getId() || null == siteETD.getUid()){
            return  ResponseUtils.errorRes("操作异常");
        }
        PUserSite siteInfo = userSiteMapper.selectByPrimaryKey(siteETD.getId());
        if(null == siteInfo){
            return ResponseUtils.errorRes("收货地址不存在");
        }
        if(!siteInfo.getUid().equals(siteETD.getUid())){
            return ResponseUtils.errorRes("操作异常");
        }
        if(siteInfo.getIsDefault().equals(new Integer(1))){
            return ResponseUtils.errorRes("当前地址已是默认地址");
        }
        UserSiteETD siteETD1 = new UserSiteETD();
        siteETD1.setId(siteInfo.getId());
        siteETD1.setUid(siteInfo.getUid());
        userSiteMapper.updateNotIsDefaultByUid(siteETD1);
        PUserSite userSiteUPD = new PUserSite();
        userSiteUPD.setId(siteInfo.getId());
        userSiteUPD.setIsDefault(1);
        if(0 == userSiteMapper.updateByPrimaryKeySelective(userSiteUPD)){
            throw new RuntimeException("删除失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public PUserSite findById(Long id) {
        return userSiteMapper.selectByPrimaryKey(id);
    }

    @Override
    public Response adminUserSiteList(UserSitePageETD sitePageETD){
        Integer pageNum = sitePageETD.getPageNo() == null ? 1 : sitePageETD.getPageNo();
        Integer pageSize = sitePageETD.getPageSize() == null ? 10 : sitePageETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<UserSitePageETD> list = userSiteMapper.selectPageList(sitePageETD);
        PageInfo pageInfo = new PageInfo(list);
        return ResponseUtils.successRes(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response userInsertSite(PUserSite userSite) {
        if(StringUtil.isEmpty(userSite.getPhone()) || !Pattern.matches("^1[123456789]\\d{9}$", userSite.getPhone())){
            return ResponseUtils.errorRes("手机号错误");
        }
        if(StringUtil.isEmpty(userSite.getSite())){
            return ResponseUtils.errorRes("收货地址错误");
        }
        if(StringUtil.isEmpty(userSite.getName())){
            return ResponseUtils.errorRes("收件人错误");
        }
        int isDefault = userSite.getIsDefault() == null ? 0 : userSite.getIsDefault();
        if(isDefault == 0){
            UserSiteETD userSiteFind = userSiteMapper.selectOneByUid(userSite.getUid());
            if(null == userSiteFind){
                isDefault = 1;
            }
        }
        PUserSite siteADD =new PUserSite();
        siteADD.setName(userSite.getName());
        siteADD.setPhone(userSite.getPhone());
        siteADD.setSite(userSite.getSite());
        siteADD.setUid(userSite.getUid());

        siteADD.setIsDefault(isDefault);
        if(0 == userSiteMapper.insertSelective(siteADD)){
            throw new BusinessException("添加失败");
        }
        if(isDefault == 1){
            UserSiteETD siteETD1 = new UserSiteETD();
            siteETD1.setId(siteADD.getId());
            siteETD1.setUid(userSite.getUid());
            userSiteMapper.updateNotIsDefaultByUid(siteETD1);
        }
        return ResponseUtils.successRes(1);
    }
}
