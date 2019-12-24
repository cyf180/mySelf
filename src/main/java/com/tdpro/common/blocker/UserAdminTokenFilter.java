package com.tdpro.common.blocker;

import com.alibaba.fastjson.JSON;
import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.exception.HttpStatus;
import com.tdpro.common.exception.NoAuthorizationException;
import com.tdpro.common.exceptions.JwtExpiredTokenException;
import com.tdpro.common.utils.FilterErrorUtils;
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
                        }
                    }
                    if (null != userInfo) {
                        if (this.userPathBlock(request)) {
                            PUserLogin userLogin = userLoginService.findById(userInfo.getUserLogId());
                            if (null == userLogin) {
                                FilterErrorUtils.notAuthorization(response,"未授权");
                                return;
                            }
                            request.setAttribute("loginId", userLogin.getId());
                            PUser user = userService.findById(userInfo.getUid());
                            if (this.ignorePathBlock(request)) {
                                if(null != user){
                                    if (!userLogin.getUid().equals(userInfo.getUid())) {
                                        FilterErrorUtils.notAuthorization(response,"用户异常");
                                        return;
                                    }
                                    request.setAttribute("uid", user.getId());
                                }else{
                                    request.setAttribute("uid", 0);
                                }
                            } else {
                                if(userLogin.getUid().equals(new Long(0))){
                                    FilterErrorUtils.notLogin(response,"未登录");
                                    return;
                                }
                                if (null == user || !userLogin.getUid().equals(userInfo.getUid())) {
                                    FilterErrorUtils.notAuthorization(response,"用户异常");
                                    return;
                                }
                                if (!user.getState().equals(new Integer(0))) {
                                    FilterErrorUtils.userDisable(response,"用户已禁用");
                                    return;
                                }
                                request.setAttribute("uid", user.getId());
                            }
                        } else if (this.adminPathBlock(request)) {
                            String urlStr = request.getRequestURI();
                            PAdmin admin = adminService.findById(userInfo.getUid());
                            if (null == admin) {
                                FilterErrorUtils.notAuthorization(response,"用户不存在");
                                return;
                            }
                            if (!admin.getState().equals(new Integer(0))) {
                                FilterErrorUtils.userDisable(response,"用户已禁用");
                                return;
                            }
                            if (!menuService.verifyUrl(urlStr, admin.getRid())) {
                                FilterErrorUtils.userDisable(response,"无权限");
                                return;
                            }
                            request.setAttribute("adminId", admin.getId());
                        } else {
                            throw new JwtExpiredTokenException("not find!");
                        }

                    }else {
                        FilterErrorUtils.notAuthorization(response,"重新授权");
                        return;
                    }
                } else {
                    FilterErrorUtils.notAuthorization(response,"未授权");
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
}
