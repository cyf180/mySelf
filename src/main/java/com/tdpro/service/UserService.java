package com.tdpro.service;

import com.tdpro.common.model.LoginRequest;
import com.tdpro.common.model.LoginResult;
import com.tdpro.common.utils.Response;
import com.tdpro.entity.POrder;
import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.*;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UserService {
    /**
     * 会员中心
     * @param uid
     * @return
     */
    Response userInformation(Long uid);

    /**
     * 我的团队
     * @param userTeamETD
     * @return
     */
    Response myTeamList(UserTeamETD userTeamETD);

    /**
     * 用户资料
     * @param uid
     * @return
     */
    Response userMaterial(Long uid);

    /**
     * 用户资料修改
     * @param userUPD
     * @return
     */
    Response updateUser(UserUPD userUPD);

    /**
     * 查询用户
     * @param id
     * @return
     */
    PUser findById(Long id);

    /**
     * 用户订单余额支付余额扣除
     * @param order
     * @return
     */
    Response userBalancePay(POrder order,PUser user);

    /**
     * 用户余额修改
     * @param balanceUpdateETD
     * @return
     */
    Boolean updateUserBalance(UserBalanceUpdateETD balanceUpdateETD);

    /**
     * 会员小程序授权认证
     *
     * @param login
     * @return
     */
    Response<LoginResult> wxLogin(LoginRequest login);

    /**
     * 用户注册
     * @param enrollETD
     * @return
     */
    Response insertUser(UserEnrollETD enrollETD);

    /**
     *
     * @return
     */
    List<PUser> findIsUserList(Integer pageNo);

    /**
     * 修改用户未会员
     * @param uid
     * @return
     */
    Boolean updateIsUser(Long uid);

    /**
     * 会员列表
     * @param userPage
     * @return
     */
    Response userPageList(UserPageETD userPage);

    /**
     * 用户详情
     * @param userInfo
     * @return
     */
    Response userInfo(UserInfoETD userInfo);

    /**
     * 修改
     * @param userInfoETD
     * @return
     */
    Response adminUpdateUser(UserInfoETD userInfoETD,Long adminId);
}
