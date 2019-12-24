package com.tdpro.common.utils;

import com.alibaba.fastjson.JSON;
import com.tdpro.common.constant.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FilterErrorUtils {

    public static void notAuthorization(HttpServletResponse response,String msg) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(),msg);
    }

    public static void notLogin(HttpServletResponse response, String msg) throws IOException {
        response.sendError(HttpStatus.PAYMENT_REQUIRED.value(),msg);
    }

    public static void  userDisable(HttpServletResponse response, String msg){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            Response result = ResponseUtils.errorRes(msg);
            writer.print(JSON.toJSONString(result));
            writer.flush();
        } catch (IOException e){
            e.getMessage();
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
