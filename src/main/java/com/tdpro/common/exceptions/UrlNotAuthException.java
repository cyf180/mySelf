package com.tdpro.common.exceptions;

/**
 * @About 用户无权限访问接口
 * @Author jy
 * @Date 2018/6/15 15:17
 */
public class UrlNotAuthException extends RuntimeException{
    private static final long serialVersionUID = -5959543387394224864L;

    public UrlNotAuthException(String message) {
        super(message);
    }
}
