package com.tdpro.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @About 在线用户信息实体
 * @Author jy
 * @Date 2018/6/6 14:08
 */
@Data
public class OnlineUserInfo implements Serializable {
    private static final long serialVersionUID = -6993441415284599853L;
    /**
     * 会员登录id
     */
    private Long uid;

    /**
     * 会员id
     */
    private Long userLogId;

    /**
     * 认证赋予角色
     */
    private String  loginRole;

    /**
     * 用户令牌
     */
    private String token;

    /**
     * 业务所需额外参数
     */
    private Object object;
}
