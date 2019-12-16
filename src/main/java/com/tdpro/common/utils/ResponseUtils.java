package com.tdpro.common.utils;

import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.constant.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Created with IntelliJ IDEA.
 * Author: lijie
 * Email: admin@elevenstyle.com
 * Date: 2018/3/26
 * Time: 16:19
 */
public class ResponseUtils {

    public static Response handleValidError(BindingResult inResult) {
        Response response = new Response(ErrorCodeConstants.ErrorCode.SYSTEM_SUCCESS.getMemo(), ErrorCodeConstants.ErrorCode.SYSTEM_SUCCESS.getErrorCode(), HttpStatus.OK,true);
        if (inResult.hasErrors()){
            StringBuffer sb = new StringBuffer();
            for (FieldError e : inResult.getFieldErrors()) {
                sb.append(e.getDefaultMessage()).append(";");
            }
            response = new Response(sb.toString(), ErrorCodeConstants.ErrorCode.VALIDATE_ERROR.getErrorCode(),HttpStatus.OK,false);
        }
        return response;
    }

    public static<T>  Response successRes(T t) {
        Response response = new Response(ErrorCodeConstants.ErrorCode.SYSTEM_SUCCESS.getMemo(), ErrorCodeConstants.ErrorCode.SYSTEM_SUCCESS.getErrorCode(),HttpStatus.OK, t,true);
        return response;
    }

    public static<T>  Response successRes(T t,ErrorCodeConstants.ErrorCode errorCode) {
        Response response = new Response(errorCode.SYSTEM_SUCCESS.getMemo(), errorCode.getErrorCode(),HttpStatus.OK, t,true);
        return response;
    }

    public static Response errorRes() {
        Response response = new Response(ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getMemo(), ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getErrorCode(),HttpStatus.INTERNAL_SERVER_ERROR, null,false);
        return response;
    }

    public static Response errorRes(String msg) {
        Response response = new Response(msg, ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getErrorCode(),HttpStatus.INTERNAL_SERVER_ERROR, null,false);
        return response;
    }
    public static Response errorResReg(ErrorCodeConstants.ErrorCode errorCode) {
        Response response = new Response(errorCode.getMemo(),errorCode.getErrorCode(),HttpStatus.INTERNAL_SERVER_ERROR, null,false);
        return response;
    }

    public static Response errorTokenResReg(ErrorCodeConstants.ErrorCode errorCode) {
        Response response = new Response(errorCode.getMemo(),errorCode.getErrorCode(),HttpStatus.UNAUTHORIZED, null,false);
        return response;
    }
}
