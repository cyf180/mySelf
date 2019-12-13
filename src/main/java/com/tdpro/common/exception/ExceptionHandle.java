package com.tdpro.common.exception;

import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.constant.HttpStatus;
import com.tdpro.common.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author zwx
 * @date 2018-6-28
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response handle(Exception e) {
            logger.error("【系统异常】{}", e);
            Response response = new Response(ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getMemo(),
                    ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getErrorCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR, false);
            response.setIsOK(false);
            return response;
    }

    /**
     * 捕捉业务异常
     * @param e
     * @return
     */
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Response handleBusniessException(Exception e) {
        if(e instanceof BusinessException) {
            logger.info("业务错误："+e.getMessage());
            return  new Response(e.getMessage(),
                    ErrorCodeConstants.ErrorCode.BUSINESS_ERROR.getErrorCode(),
                    HttpStatus.OK,false);
        }
        return new Response(ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getMemo(),
                ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getErrorCode(),
                HttpStatus.INTERNAL_SERVER_ERROR,false);
    }


    /**
     * 捕捉参数异常
     * @param e
     * @return
     */
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @ExceptionHandler(value = ParamJsonException.class)
    @ResponseBody
    public Response handleParam(Exception e) {
        if(e instanceof ParamJsonException) {
            logger.info("参数错误："+e.getMessage());
            return  new Response(e.getMessage(),
                    ErrorCodeConstants.ErrorCode.PARAM_ERROR.getErrorCode(),HttpStatus.OK,false);
        }
        return new Response(ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getMemo(),
                ErrorCodeConstants.ErrorCode.SYSTEM_ERROR.getErrorCode(),
                HttpStatus.OK,false);
    }

}
