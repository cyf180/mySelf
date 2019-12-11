package com.tdpro.common.blocker;

import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.exceptions.JwtExpiredTokenException;
import com.tdpro.common.utils.JwtTokenUtil;
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
                    if (this.userPathBlock(request)) {
                        PUserLogin userLogin = userLoginService.findById(userInfo.getUserLogId());
                        if (null == userLogin) {
                            throw new RuntimeException("未授权");
                        }
                        if (this.ignorePathBlock(request)) {
                            request.setAttribute("uid", 0);
                            request.setAttribute("loginId", userLogin.getId());
                        } else {
                            if (null == userInfo || null == userInfo.getUid() || userInfo.getUid().equals(new Long(0))) {
                                throw new RuntimeException("未登录");
                            }
                            PUser user = userService.findById(userInfo.getUid());
                            if (null == user || !userLogin.getUid().equals(userInfo.getUid())) {
                                throw new RuntimeException("用户异常");
                            }
                            if (!user.getState().equals(new Integer(0))) {
                                throw new RuntimeException("用户已禁用");
                            }
                            request.setAttribute("uid", user.getId());
                            request.setAttribute("loginId", userLogin.getId());
                        }
                    } else if (this.adminPathBlock(request)) {
                        String urlStr = request.getRequestURI();
                        PAdmin admin = adminService.findById(userInfo.getUid());
                        if(null == admin){
                            throw new RuntimeException("用户不存在");
                        }
                        if(!admin.getState().equals(new Integer(0))){
                            throw new RuntimeException("用户已禁用");
                        }
                        if(!menuService.verifyUrl(urlStr,admin.getRid())){
                            throw new RuntimeException("无权限");
                        }
                        request.setAttribute("adminId", admin.getId());
                    } else {
                        throw new JwtExpiredTokenException("not find!");
                    }
                }else {
                    throw new JwtExpiredTokenException("no!");
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
