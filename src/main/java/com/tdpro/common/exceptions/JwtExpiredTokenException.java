package com.tdpro.common.exceptions;

import com.tdpro.common.utils.jwt.JwtToken;

/**
 * @About
 * @Author jy
 * @Date 2018/6/6 16:12
 */
public class JwtExpiredTokenException extends RuntimeException{
    private static final long serialVersionUID = -5959543783324224864L;

    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
