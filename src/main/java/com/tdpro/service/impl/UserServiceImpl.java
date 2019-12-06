package com.tdpro.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.model.LoginRequest;
import com.tdpro.common.model.LoginResult;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserLogin;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.*;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
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
import java.util.regex.Pattern;

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
    @Autowired
    private SmsCodeService codeService;
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
        Long accountId = 0L;
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
            onlineUserInfo.setUserLogId(tThirdLogin.getId());
            onlineUserInfo.setUid(accountId);
            // 存入微信sessionkey
            onlineUserInfo.setObject(cacheData);
            String accessToken = jwtService.createToken(onlineUserInfo);
            if (StringUtils.hasText(accessToken)) {
                //存入缓存
                onlineUserInfo.setToken(accessToken);
//                Boolean redis = redisServer.cachAccountInfo(onlineUserInfo, RedisKey.REDIS_ACCOUNT_ONLINE);
                loginResult.setToken(accessToken);
            }
            resultResponse=ResponseUtils.successRes(loginResult);
        }
        return resultResponse;
    }

    @Override
    public Response insertUser(UserEnrollETD userETD) {
        Long userLoginId= userETD.getLoginId();
        LoginResult loginResult = new LoginResult();
        loginResult.setIsBindPhone(0);
        if(StringUtil.isEmpty(userETD.getPhone()) || !Pattern.matches("^1[123456789]\\d{9}$", userETD.getPhone())){
            return ResponseUtils.errorRes("手机号错误");
        }
        PUser userFindByPhone = userMapper.findByPhone(userETD.getPhone());
        if(null != userFindByPhone){
            return ResponseUtils.errorRes("该手机号已存在");
        }
        loginResult.setPhone(userETD.getPhone());
        if(StringUtil.isEmpty(userETD.getPayPassword()) || !Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$", userETD.getPayPassword())){
            return ResponseUtils.errorRes("支付密码格式错误");
        }

        if(!codeService.codeVerification(userETD.getPhone(),userETD.getCode())){
            return ResponseUtils.errorRes("验证码错误或已过期");
        }
        String payPwd = DigestUtil.md5Hex(userETD.getPayPassword());
        PUserLogin userLogin = userLoginService.findById(userLoginId);
        if(null == userLogin){
            throw  new RuntimeException("授权异常");
        }
        OnlineUserInfo onlineUserInfo = new OnlineUserInfo();
        if(!userLogin.getUid().equals(new Long(0))){
            return  ResponseUtils.errorRes("该微信已绑定手机号");
        }else{
            PUser userADD = new PUser();
            userADD.setPhone(userETD.getPhone());
            userADD.setIsUser(0);
            userADD.setState(0);
            userADD.setPayPassword(payPwd);
            userADD.setCreateTime(new Date());
            if(null != userETD.getStrawUid()){
                PUser strawUser = this.findById(userETD.getStrawUid());
                if(null == strawUser){
                    return ResponseUtils.errorRes("推荐人不存在");
                }
                userADD.setStrawUid(userETD.getStrawUid());
            }
            if(0==userMapper.insertSelective(userADD)){
                throw new RuntimeException("用户添加失败");
            }
            PUserLogin userLoginUPD = new PUserLogin();
            userLoginUPD.setId(userLogin.getId());
            userLoginUPD.setUid(userADD.getId());
            if(!userLoginService.updateUserLogin(userLoginUPD)){
                throw new RuntimeException("绑定用手机失败");
            }
            onlineUserInfo.setUid(userADD.getId());
            onlineUserInfo.setUserLogId(userLogin.getId());
            onlineUserInfo.setLoginRole("USER_ROLE");
            loginResult.setIsBindPhone(1);
            loginResult.setKey(userLogin.getOpenId());
            String accessToken = jwtService.createToken(onlineUserInfo);
            if (StringUtils.hasText(accessToken)) {
                onlineUserInfo.setToken(accessToken);
                loginResult.setToken(accessToken);
            }else{
                return ResponseUtils.errorRes("token生成失败");
            }
        }
        return ResponseUtils.successRes(loginResult);
    }
}
