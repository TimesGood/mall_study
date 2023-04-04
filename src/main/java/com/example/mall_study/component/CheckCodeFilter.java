package com.example.mall_study.component;

import com.example.mall_study.exception.CaptchaException;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 用于验证码验证
 */
public class CheckCodeFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLoginFailureHandler.class);

    @Autowired
    JwtLoginFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if("/admin/login".equals(uri) && request.getMethod().equals("POST")) {
            LOGGER.info("开始验证验证码");
            try {
                validate(request);
                filterChain.doFilter(request,response);
            } catch (AuthenticationException e) {
                failureHandler.onAuthenticationFailure(request,response,e);
            }
            return;
        }
        filterChain.doFilter(request,response);
    }
    //后面有验证码的时候再完善
    private void validate(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String checkCode = request.getParameter("checkCode");
        if(StringUtils.isEmpty(checkCode)){
            throw new CaptchaException("验证码不能为空");
        }
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        if(Objects.isNull(checkCode_session)){
            throw new CaptchaException("验证码不存在");
        }
        if(checkCode.equals(checkCode_session)){
            throw new CaptchaException("验证码错误");
        }
    }
}
