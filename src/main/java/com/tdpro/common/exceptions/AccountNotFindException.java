package com.tdpro.common.exceptions;

/**
 * @About 用户未绑定手机
 * @Author jy
 * @Date 2018/6/15 15:17
 */
public class AccountNotFindException extends RuntimeException{
    private static final long serialVersionUID = -5959543387324224864L;

    public AccountNotFindException(String message) {
        super(message);
    }
}
