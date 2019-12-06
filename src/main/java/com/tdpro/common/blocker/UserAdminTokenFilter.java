package com.tdpro.common.blocker;

import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.exceptions.JwtExpiredTokenException;
import com.tdpro.common.utils.JwtTokenUtil;
import com.tdpro.entity.PUser;
import com.tdpro.entity.PUserLogin;
import com.tdpro.service.PUserLoginService;
import com.tdpro.service.UserService;
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
    @Value("${com.tdpro.common.utils.jwt.headerString}")
    private String headerString;
    @Autowired
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    @Autowired
    private UserService userService;
    @Autowired
    private PUserLoginService userLoginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            boolean pathBlock = this.pathBlock(request);
            if (pathBlock) {
                String headerToken = request.getHeader(headerString);
                request.setAttribute("uid", 1);
                if (!StringUtils.isEmpty(headerToken)) {
                    OnlineUserInfo userInfo = null;
                    final String requestHeader = request.getHeader(headerString);
                    String authToken = null;
                    Claims claims=null;
                    Boolean isAuth=false;
                    if (requestHeader != null && requestHeader.startsWith("Bearer ")){
                        authToken = requestHeader.substring(7);
                        try {
                            if (StringUtils.hasText(authToken)&&!authToken.equals("null")){
                                claims = jwtTokenUtil.getClaimsFromToken(authToken);
                                if (claims!=null){
                                    JSONObject jsonObject = JSONObject.fromObject(claims.getSubject());
                                    userInfo=(OnlineUserInfo) JSONObject.toBean(jsonObject, OnlineUserInfo.class);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            isAuth=true;
                        }
                    }
                    PUserLogin userLogin = userLoginService.findById(userInfo.getUserLogId());
                    if(null == userLogin){
                        throw new RuntimeException("未授权");
                    }
                    if(this.ignorePathBlock(request)) {
                        request.setAttribute("uid", 0);
                        request.setAttribute("loginId", userLogin.getId());
                    }else{
                        if(null == userInfo || null == userInfo.getUid() ||  userInfo.getUid().equals(new Long(0))){
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
                } else {
                    throw new JwtExpiredTokenException("no!");
                }

            }

        }
        filterChain.doFilter(request, response);

    }

    private boolean pathBlock(HttpServletRequest request) {
        String urlStr = request.getRequestURI();
        for (String str : userPath) {
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
