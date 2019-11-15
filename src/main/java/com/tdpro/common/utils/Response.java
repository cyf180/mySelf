package com.tdpro.common.utils;

import com.tdpro.common.constant.HttpStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author: lijie
 * Email: admin@elevenstyle.com
 * Date: 2018/3/23
 * Time: 14:35
 */
@Data
public class Response<T> implements Serializable{

    private static final long serialVersionUID = -6993441415284590853L;
    //错误信息
    private String msg;

    //调用状态码
    private final String code;

    //是否成功
    private final HttpStatus status;

    private Boolean isOK;

    //数据
    private T data;

    public Response(String msg, final String code, final HttpStatus status, Boolean isOk) {
        this.msg = msg;
        this.code = code;
        this.status = status;
        this.isOK = isOk;
    }

    public Response(String msg, final String code, final HttpStatus status, T data, Boolean isOK) {
        this.msg = msg;
        this.code = code;
        this.status = status;
        this.data = data;
        this.isOK = isOK;
    }

}
