package com.example.mall_study.component.filter;

import com.example.mall_study.component.handler.JwtLoginFailureHandler;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于验证码验证
 */
public class CaptchaFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLoginFailureHandler.class);

    @Autowired
    JwtLoginFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("开始验证码验证");
        String uri = request.getRequestURI();
        if("/admin/login".equals(uri) && request.getMethod().equals("POST")) {
            try {
                validate(request);
            } catch (/*CaptchaException e*/ AuthenticationException e) {
                failureHandler.onAuthenticationFailure(request,response,e);
            }
        }
        filterChain.doFilter(request,response);
    }
    //后面有验证码的时候再完善
    private void validate(HttpServletRequest request) {
        String key = request.getParameter("tokens");
        String code = request.getParameter("code");
        if(StringUtils.isBlank(key) || StringUtils.isBlank(code)) {
//            throw CaptchaException("验证码信息为空");
        }
//        if(!code.equals(RCaptchaFilteredisUtil.hget("","key"))){
//            throw CaptchaException("验证码错误");
//        }
//        redisUtil.del("key");
    }
}
