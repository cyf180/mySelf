package com.tdpro.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.model.LoginRequest;
import com.tdpro.common.model.LoginResult;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserLogin;
import com.tdpro.entity.extend.UserBalanceUpdateETD;
import com.tdpro.entity.extend.UserETD;
import com.tdpro.entity.extend.UserTeamETD;
import com.tdpro.entity.extend.UserUPD;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.DealLogService;
import com.tdpro.service.JwtService;
import com.tdpro.service.PUserLoginService;
import com.tdpro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private PUserMapper userMapper;
    @Autowired
    private DealLogService dealLogService;
    @Autowired
    private WeiXinManageServiceImpl weiXinManageService;
    @Autowired
    private PUserLoginService userLoginService;
    @Autowired
    private JwtService jwtService;
    private Lock lock = new ReentrantLock();

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

    @Override
    public PUser findById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Response userBalancePay(POrder order,PUser user) {
        BigDecimal realPrice = order.getRealPrice();
        BigDecimal userOldBalance = user.getBalance();
        BigDecimal userEndBalance = userOldBalance.subtract(realPrice);
        if(userEndBalance.compareTo(new BigDecimal("0")) < 0){
            return ResponseUtils.errorRes("用户余额不足");
        }
        UserBalanceUpdateETD userBalanceUPD = new UserBalanceUpdateETD();
        userBalanceUPD.setId(order.getUid());
        userBalanceUPD.setBalance(userEndBalance);
        userBalanceUPD.setOldBalance(userOldBalance);
        if(0 == userMapper.updateBalance(userBalanceUPD)){
            user = this.findById(user.getId());
            userOldBalance = user.getBalance();
            userEndBalance = userOldBalance.subtract(realPrice);
            if(userEndBalance.compareTo(new BigDecimal("0")) < 0){
                return ResponseUtils.errorRes("用户余额不足");
            }
            userBalanceUPD.setBalance(userEndBalance);
            userBalanceUPD.setOldBalance(userOldBalance);
            if(0 == userMapper.updateBalance(userBalanceUPD)){
                throw new RuntimeException("余额修改失败");
            }
        }
        if(!dealLogService.insertDealLog("余额支付",order.getId(),realPrice,userEndBalance,user.getId(),null)){
            throw new RuntimeException("添加交易记录失败");
        }
        return null;
    }

    @Override
    public Boolean updateUserBalance(UserBalanceUpdateETD balanceUpdateETD){
        if(0 == userMapper.updateBalance(balanceUpdateETD)){
            return false;
        }
        return true;
    }

    @Override
    public Response<LoginResult> wxLogin(LoginRequest login) {
        Response<LoginResult> resultResponse = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_ERROR);
        LoginResult loginResult = new LoginResult();
        loginResult.setIsBindPhone(0);
        //判断参数的合法性
        if (login == null || !StringUtils.hasText(login.getCode())) {
            return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.PARA_NORULE_ERROR);
        }
        int result = 0;
        Long accountId = null;
        PUserLogin tThirdLogin = null;
        String openId = "";
        //调用微信授权获取openid
        JSONObject cacheData = new JSONObject();
        try {
            lock.lock();
            WxMaJscode2SessionResult session = weiXinManageService.getSessionInfo(login.getCode());
            if (session != null && session.getOpenid() != null) {
                openId = session.getOpenid();
                // 微信sessionkey
                cacheData.put("sessionkey", session.getSessionKey());
                //openId="1234567";
                tThirdLogin = userLoginService.findByOpenId(openId);
                if (tThirdLogin == null) {
                    //插入用户登录信息
                    tThirdLogin = new PUserLogin();
                    tThirdLogin.setOpenId(openId);
                    tThirdLogin.setUid(0L);
                    result = userLoginService.insertUserLog(tThirdLogin);
                } else {
                    result = 1;
                }
            } else {
                //用户微信授权失败
                return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.APPLICATION_OPER_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_ERROR);
        }finally {
            lock.unlock();
        }
        if (result == 1) {
            //校验用户是否已绑定手机号
            if (tThirdLogin.getUid() != null && tThirdLogin.getUid() > 0) {
                PUser user = userMapper.selectByPrimaryKey(tThirdLogin.getUid());
                if (user != null) {
                    accountId = user.getId();
                    //已绑定
                    loginResult.setIsBindPhone(1);
                    loginResult.setPhone(user.getPhone());
                    loginResult.setKey(openId);
                }
            }
            // 验证成功生成token
            OnlineUserInfo onlineUserInfo = new OnlineUserInfo();
            onlineUserInfo.setLoginRole("USER_ROLE");
            onlineUserInfo.setId(tThirdLogin.getId());
            onlineUserInfo.setAccountId(accountId);
            // 存入微信sessionkey
            onlineUserInfo.setObject(cacheData);
            String accessToken = jwtService.createToken(onlineUserInfo);
            if (StringUtils.hasText(accessToken)) {
                //存入缓存
                onlineUserInfo.setToken(accessToken);
//                Boolean redis = redisServer.cachAccountInfo(onlineUserInfo, RedisKey.REDIS_ACCOUNT_ONLINE);
                loginResult.setToken(accessToken);
            }
        }
        return resultResponse;
    }
}
