package com.tdpro.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.java.emoji.EmojiConverter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.QiniuSimpleUpload;
import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.constant.IssueType;
import com.tdpro.common.model.LoginRequest;
import com.tdpro.common.model.LoginResult;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.config.weixin.WxMaProperties;
import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserLogin;
import com.tdpro.entity.PUserPayConfig;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.PUserMapper;
import com.tdpro.service.*;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${wechat.mainapp.appid}")
    private String appid;
    @Value("${wechat.mainapp.secret}")
    private String secret;
    @Value("${qiniu.imgPath}")
    private String imgPath;
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
    @Autowired
    private UserPayConfigService userPayConfigService;
    @Autowired
    private LogService logService;
    @Autowired
    private UserVoucherService userVoucherService;
    WxMaProperties wxMaProperties=new WxMaProperties();
    WxMaService wxMaService = new WxMaServiceImpl();
    private EmojiConverter emojiConverter = EmojiConverter.getInstance();
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
            if(StringUtil.isNotEmpty(user.getPayPassword())){
               String payPwd = DigestUtil.md5Hex(user.getPayPassword());
                userUPD.setPayPassword(payPwd);
            }
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
    public Response userBalancePay(POrder order,PUser user,BigDecimal realPrice) {
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
        loginResult.setIsUser(0);
        //判断参数的合法性
        if (login == null || !StringUtils.hasText(login.getCode())) {
            return ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.PARA_NORULE_ERROR);
        }
        PUserPayConfig payConfig = userPayConfigService.findByType(0);
        if(null != payConfig){
            loginResult.setBuyUserPrice(payConfig.getPrice());
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
                    if(StringUtil.isNotEmpty(login.getWxHead())){
                        tThirdLogin.setHeadPath(login.getWxHead());
                    }
                    if(StringUtil.isNotEmpty(login.getWxName())){
                        tThirdLogin.setNickName(login.getWxName());
                    }
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
                    loginResult.setIsUser(user.getIsUser());
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
//        if(StringUtil.isEmpty(userETD.getPayPassword()) || !Pattern.matches("\\d{5}$ ", userETD.getPayPassword())){
//            return ResponseUtils.errorRes("请输入6位数字密码");
//        }

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
            Long strawUid = null;
            boolean issue = false;
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
                strawUid=strawUser.getId();
                issue = true;
            }else{
                PUser strawUser = userMapper.findOne();
                if(null == strawUser){
                    return ResponseUtils.errorRes("系统尚未开启");
                }
                strawUid = strawUser.getId();
            }

            String nikName = emojiConverter.toHtml(userETD.getNickName());
            userADD.setStrawUid(strawUid);
            userADD.setNickName(nikName);
            userADD.setWxQrCode("");
            if(0==userMapper.insertSelective(userADD)){
                throw new RuntimeException("用户添加失败");
            }
            String qrCode = this.getQrCode(userADD.getId());
            if(StringUtil.isNotEmpty(qrCode)){
                PUser userUPD = new PUser();
                userUPD.setId(userADD.getId());
                userUPD.setWxQrCode(qrCode);
                if(0 == userMapper.updateByPrimaryKeySelective(userUPD)){
                    throw new RuntimeException("修改二维码失败");
                }
            }
            PUserLogin userLoginUPD = new PUserLogin();
            userLoginUPD.setId(userLogin.getId());
            userLoginUPD.setUid(userADD.getId());
            userLoginUPD.setHeadPath(userETD.getHeadPath());
            userLoginUPD.setNickName(nikName);
            if(!userLoginService.updateUserLogin(userLoginUPD)){
                throw new RuntimeException("绑定用手机失败");
            }
            if(issue){
                userVoucherService.voucherIssue(strawUid,userADD.getId(),IssueType.ENROLL_TYPE);
            }
            onlineUserInfo.setUid(userADD.getId());
            onlineUserInfo.setUserLogId(userLogin.getId());
            onlineUserInfo.setLoginRole("USER_ROLE");
            loginResult.setIsBindPhone(1);
            loginResult.setUid(userADD.getId());
            loginResult.setKey(userLogin.getOpenId());
            if(StringUtil.isNotEmpty(qrCode)){
                loginResult.setQrCode(imgPath+qrCode);
            }

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

    @Override
    public List<PUser> findIsUserList(Integer pageNo){
        PageHelper.startPage(pageNo,1000);
        return userMapper.findListIsUser();
    }

    @Override
    public Boolean updateIsUser(Long uid) {
        PUser userUPD = new PUser();
        userUPD.setId(uid);
        userUPD.setIsUser(1);
        if(0 == userMapper.updateByPrimaryKeySelective(userUPD)){
            return false;
        }
        return true;
    }

    @Override
    public Response userPageList(UserPageETD userPage) {
        Integer pageNum = userPage.getPageNo() == null ? 1 : userPage.getPageNo();
        Integer pageSize = userPage.getPageSize() == null ? 10 : userPage.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        if(StringUtil.isNotEmpty(userPage.getStartTimes())){
            try {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userPage.setStartTime(ft.parse(userPage.getStartTimes()));
            } catch (ParseException e) {
               e.printStackTrace();
            }
        }
        if(StringUtil.isNotEmpty(userPage.getEndTimes())){
            try {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userPage.setEndTime(ft.parse(userPage.getEndTimes()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<UserPageETD> list = userMapper.selectPageList(userPage);
        Map map = new HashMap();
        map.put("pageInfo",new PageInfo(list));
        map.put("queryModel",userPage);
        return ResponseUtils.successRes(map);
    }

    @Override
    public Response userInfo(UserInfoETD userInfo) {
        UserInfoETD userInfoFind = userMapper.findInfoById(userInfo.getId());
        if(null == userInfoFind){
            return ResponseUtils.errorRes("用户不存在");
        }
        return ResponseUtils.successRes(userInfoFind);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response adminUpdateUser(UserInfoETD userInfoETD,Long adminId){
        if(null == userInfoETD.getId()){
            return ResponseUtils.errorRes("关键值错误");
        }
        PUser userFind = this.findById(userInfoETD.getId());
        if(null == userFind){
            return ResponseUtils.errorRes("用户错误");
        }
        boolean isUpd = false;
        PUser userUPD = new PUser();
        userUPD.setId(userFind.getId());

        if(StringUtil.isNotEmpty(userInfoETD.getName()) && !userInfoETD.getName().equals(userFind.getName())){
            userUPD.setName(userInfoETD.getName());
            isUpd = true;
        }
        if(StringUtil.isNotEmpty(userInfoETD.getIdCard()) && !userInfoETD.getIdCard().equals(userFind.getIdCard())){
            userUPD.setIdCard(userInfoETD.getIdCard());
            isUpd = true;
        }
        if(StringUtil.isNotEmpty(userInfoETD.getBankName()) && !userInfoETD.getBankName().equals(userFind.getBankName())){
            userUPD.setBankName(userInfoETD.getBankName());
            isUpd = true;
        }
        if(StringUtil.isNotEmpty(userInfoETD.getBankBranch()) && !userInfoETD.getBankBranch().equals(userFind.getBankBranch())){
            userUPD.setBankBranch(userInfoETD.getBankBranch());
            isUpd = true;
        }
        if(StringUtil.isNotEmpty(userInfoETD.getBankCard()) && !userInfoETD.getBankCard().equals(userFind.getBankCard())){
            userUPD.setBankCard(userInfoETD.getBankCard());
            isUpd = true;
        }
        if(null != userInfoETD.getState() && !userInfoETD.getState().equals(userFind.getState())){
            userUPD.setState(userInfoETD.getState());
            isUpd = true;
        }
        if(null != userInfoETD.getStrawPhone()){
            PUser strawUser = userMapper.findByPhone(userInfoETD.getStrawPhone());
            if(null == strawUser){
                return ResponseUtils.errorRes("推荐人不存在");
            }
            if(!userFind.getStrawUid().equals(strawUser.getId())){
                userUPD.setStrawUid(strawUser.getId());
                isUpd = true;
            }
        }
        if(isUpd){
            logService.insertLog(adminId,"后台修改会员信息","修改信息："+userInfoETD.toString(),1);
            if(0 == userMapper.updateByPrimaryKeySelective(userUPD)){
                throw new RuntimeException("修改失败");
            }
        }
        if(null != userInfoETD.getBalance()){
            if(userInfoETD.getBalance().compareTo(new BigDecimal("0")) < 0){
                return ResponseUtils.errorRes("金额异常");
            }
            StringBuffer note = new StringBuffer();
            note.append("修改会员：").append(userFind.getPhone()).append(",修改前余额：").append(userFind.getBalance()).append(",修改余额为：").append(userInfoETD.getBalance());
            logService.insertLog(adminId,"后台修改会员余额",note.toString(),1);
            UserBalanceUpdateETD userBalanceUPD = new UserBalanceUpdateETD();
            userBalanceUPD.setId(userFind.getId());
            userBalanceUPD.setBalance(userInfoETD.getBalance());
            userBalanceUPD.setOldBalance(userFind.getBalance());
            if(0 == userMapper.updateBalance(userBalanceUPD)){
                throw new RuntimeException("余额修改失败");
            }
        }
        return ResponseUtils.successRes(1);
    }

    private String getQrCode(Long uid){
        File file = new File("");
        QiniuSimpleUpload qiniuSimpleUpload=new QiniuSimpleUpload();
        String qrCodePath = null;
        try {
            wxMaProperties.setAppid(appid);   //需要替换为实际商户appid
            wxMaProperties.setSecret(secret); //需要替换为实际商户Secret
            wxMaProperties.setExpiresTime(System.currentTimeMillis());
            wxMaService.setWxMaConfig(wxMaProperties);
            file = wxMaService.getQrcodeService().createWxCodeLimit("pages/index/index",uid.toString());
            String imgName=System.currentTimeMillis()+".png";
            String upResult = qiniuSimpleUpload.upload(file, imgName);
            JSONObject formatUpResult = JSON.parseObject(upResult);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(formatUpResult.get("key").toString())) {
                qrCodePath = formatUpResult.get("key").toString();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return qrCodePath;
    }
}
