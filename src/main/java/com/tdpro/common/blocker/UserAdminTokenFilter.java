package com.tdpro.common.blocker;

import com.alibaba.fastjson.JSON;
import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.exception.NoAuthorizationException;
import com.tdpro.common.exceptions.JwtExpiredTokenException;
import com.tdpro.common.utils.JwtTokenUtil;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PAdmin;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserLogin;
import com.tdpro.service.*;
import io.jsonwebtoken.Claims;
import io.swagger.models.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserAdminTokenFilter extends OncePerRequestFilter {
    @Value("${blockerPath.userPath}")
    private String[] userPath;
    @Value("${blockerPath.userIgnorePath}")
    private String[] userIgnorePath;
    @Value("${blockerPath.adminPath}")
    private String[] adminPath;
    @Value("${blockerPath.notTokenPath}")
    private String[] notTokenPath;
    @Value("${com.tdpro.common.utils.jwt.headerString}")
    private String headerString;
    @Autowired
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    @Autowired
    private UserService userService;
    @Autowired
    private PUserLoginService userLoginService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private MenuService menuService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            String headerToken = request.getHeader(headerString);
            if (!this.notToken(request)) {
                if (!StringUtils.isEmpty(headerToken)) {
                    OnlineUserInfo userInfo = null;
                    final String requestHeader = request.getHeader(headerString);
                    String authToken = null;
                    Claims claims = null;
                    Boolean isAuth = false;
                    if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                        authToken = requestHeader.substring(7);
                        try {
                            if (StringUtils.hasText(authToken) && !authToken.equals("null")) {
                                claims = jwtTokenUtil.getClaimsFromToken(authToken);
                                if (claims != null) {
                                    JSONObject jsonObject = JSONObject.fromObject(claims.getSubject());
                                    userInfo = (OnlineUserInfo) JSONObject.toBean(jsonObject, OnlineUserInfo.class);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            isAuth = true;
                        }
                    }
                    if (null != userInfo) {
                        if (this.userPathBlock(request)) {
                            PUserLogin userLogin = userLoginService.findById(userInfo.getUserLogId());
                            if (null == userLogin) {
                                returnJson(response,"未授权");
                                return;
                            }
                            if (this.ignorePathBlock(request)) {
                                request.setAttribute("uid", 0);
                                request.setAttribute("loginId", userLogin.getId());
                            } else {
                                if(userLogin.getUid().equals(new Long(0))){
                                    notLogJson(response,"未登录");
                                    return;
                                }
                                PUser user = userService.findById(userInfo.getUid());
                                if (null == user || !userLogin.getUid().equals(userInfo.getUid())) {
                                    returnJson(response,"用户异常");
                                    return;
                                }
                                if (!user.getState().equals(new Integer(0))) {
                                    disableJson(response,"用户已禁用");
                                    return;
                                }
                                request.setAttribute("uid", user.getId());
                                request.setAttribute("loginId", userLogin.getId());
                            }
                        } else if (this.adminPathBlock(request)) {
                            String urlStr = request.getRequestURI();
                            PAdmin admin = adminService.findById(userInfo.getUid());
                            if (null == admin) {
                                returnJson(response,"用户不存在");
                                return;
                            }
                            if (!admin.getState().equals(new Integer(0))) {
                                disableJson(response,"用户已禁用");
                                return;
                            }
                            if (!menuService.verifyUrl(urlStr, admin.getRid())) {
                                disableJson(response,"无权限");
                                return;
                            }
                            request.setAttribute("adminId", admin.getId());
                        } else {
                            throw new JwtExpiredTokenException("not find!");
                        }

                    }else {
                        returnJson(response,"重新授权");
                        return;
                    }
                } else {
                    returnJson(response,"未授权");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);

    }

    private boolean userPathBlock(HttpServletRequest request) {
        String urlStr = request.getRequestURI();
        for (String str : userPath) {
            String re = str;
            if (urlStr.startsWith(re)) {
                return true;
            }
        }
        return false;
    }

    private boolean adminPathBlock(HttpServletRequest request) {
        String urlStr = request.getRequestURI();
        for (String str : adminPath) {
            String re = str;
            if (urlStr.startsWith(re)) {
                return true;
            }
        }
        return false;
    }

    private boolean notToken(HttpServletRequest request) {
        String urlStr = request.getRequestURI();
        for (String str : notTokenPath) {
            String re = str;
            if (urlStr.startsWith(re)) {
                return true;
            }
        }
        return false;
    }

    private boolean ignorePathBlock(HttpServletRequest request) {
        String urlStr = request.getRequestURI();
        for (String str : userIgnorePath) {
            String re = str;
            if (urlStr.startsWith(re)) {
                return true;
            }
        }
        return false;
    }

    private void returnJson(HttpServletResponse response,String msg){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setStatus(401);
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

    private void notLogJson(HttpServletResponse response,String msg){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setStatus(402);
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

    private void disableJson(HttpServletResponse response,String msg){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
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
