package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.UserETD;
import com.tdpro.entity.extend.UserTeamETD;
import com.tdpro.entity.extend.UserUPD;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public Response myTeamList(UserTeamETD userTeamETD) {
        Integer pageNo = userTeamETD.getPageNo() == null ? 1 : userTeamETD.getPageNo();
        Integer pageSize = userTeamETD.getPageSize() == null ? 10: userTeamETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<UserTeamETD> teamList = userMapper.userTeamList(userTeamETD.getId());
        PageInfo pageInfo = new PageInfo(teamList);
        return ResponseUtils.successRes(pageInfo);
    }

    @Override
    public Response userMaterial(Long uid) {
        PUser userInfo = userMapper.selectByPrimaryKey(uid);
        if(null == userInfo){
            return ResponseUtils.errorRes("信息错误");
        }
        return ResponseUtils.successRes(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response updateUser(UserUPD user) {
        Response response;
        PUser userInfo = userMapper.selectByPrimaryKey(user.getId());
        if(null != userInfo){
            PUser userUPD = new PUser();
            userUPD.setId(userInfo.getId());
            userUPD.setName(user.getName());
            userUPD.setBankName(user.getBankName());
            userUPD.setBankBranch(user.getBankBranch());
            userUPD.setBankCard(user.getBankCard());
            userUPD.setIdCard(user.getIdCard());
            if(0 == userMapper.updateByPrimaryKeySelective(userUPD)){
                throw new RuntimeException("修改失败");
            }else{
                response = ResponseUtils.successRes(1);
            }
        }else{
            response=ResponseUtils.errorRes("用户不存在");
        }
        return response;
    }
}
