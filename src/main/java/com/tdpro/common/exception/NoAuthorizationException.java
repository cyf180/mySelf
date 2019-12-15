package com.tdpro.common.exception;

/**
 * Created with IntelliJ IDEA.
 * Author:
 * Email:
 * Date: 2019/1/10
 * Time: 11:54
 */
public class NoAuthorizationException extends RuntimeException{

    public NoAuthorizationException(String msg) {
        super(msg);
    }
}
