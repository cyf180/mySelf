package com.tdpro.common.exception;

/**
 * 参数异常
 * @author zwx
 * @since 2018-06-18
 */
public class ParamJsonException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public ParamJsonException() {}

    public ParamJsonException(String message) {
        super(message);
        this.message = message;
    }


}
