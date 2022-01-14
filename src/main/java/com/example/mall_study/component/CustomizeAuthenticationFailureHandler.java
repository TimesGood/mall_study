package com.example.mall_study.component;

import cn.hutool.json.JSONUtil;
import com.example.mall_study.common.api.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆失败时自定义处理逻辑
 */
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizeAuthenticationFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LOGGER.info("登录失败处理");
        CommonResult<String> unauthorized = null;
        //可根据UserDetailsServiceImpl中抛出的异常返回不同信息
        if (exception instanceof AccountExpiredException) {
            unauthorized = CommonResult.validateFailed("账号过期");
            //账号过期
        } else if (exception instanceof BadCredentialsException) {
            unauthorized = CommonResult.validateFailed("用户名或密码错误");
            //密码错误
        } else{
            unauthorized = CommonResult.validateFailed("未知错误");
        }
        System.out.println(unauthorized);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSONUtil.parse(unauthorized));
        response.getWriter().flush();
    }
}
