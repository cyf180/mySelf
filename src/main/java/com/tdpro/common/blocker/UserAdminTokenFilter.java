package com.tdpro.common.blocker;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tdpro.common.OnlineUserInfo;
import com.tdpro.common.constant.Scopes;
import com.tdpro.common.exceptions.JwtExpiredTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAdminTokenFilter extends OncePerRequestFilter {
    @Value("${blockerPath.userPath}")
    private String[] userPath;
    @Value("${com.tdpro.common.utils.jwt.headerString}")
    private String headerString;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean pathBlock = this.pathBlock(request);
        if(pathBlock){
            String headerToken = request.getHeader(headerString);
            request.setAttribute("uid", 1);
//            if(!StringUtils.isEmpty(headerToken)){
//                request.setAttribute("uid", 0);
//            }else {
//                throw new JwtExpiredTokenException("no!");
//            }
        }
        filterChain.doFilter(request, response);

    }

    private boolean pathBlock(HttpServletRequest request){
        String urlStr = request.getRequestURI();
        for(String str:userPath) {
            String re = str;
            if(urlStr.startsWith(re)) {
                return true;
            }
        }
        return false;
    }
}
