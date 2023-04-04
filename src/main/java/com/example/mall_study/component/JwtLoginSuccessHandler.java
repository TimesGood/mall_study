package com.example.mall_study.component;

import cn.hutool.json.JSONUtil;
import com.example.mall_study.common.api.CommonResult;
import com.example.mall_study.common.util.JwtTokenUtil;
import com.example.mall_study.common.util.RedisUtil;
import com.example.mall_study.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功后处理逻辑
 */
@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLoginSuccessHandler.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOGGER.info("登录成功处理");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //制作token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenPrefix", jwtProperties.getTokenPrefix());
        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展
        //处理编码方式，防止中文乱码的情况
        response.setContentType("application/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().print(JSONUtil.parse(CommonResult.success(tokenMap)));
        response.getWriter().flush();
    }
}
