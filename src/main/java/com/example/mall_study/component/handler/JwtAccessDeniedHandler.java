package com.example.mall_study.component.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.mall_study.common.api.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拒绝访问，权限不足处理逻辑
 */

public class JwtAccessDeniedHandler implements AccessDeniedHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        LOGGER.info("用户没有权限处理");
        //允许跨域响应头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(JSONObject.toJSON(CommonResult.forbidden(e.getMessage())));
        response.getWriter().flush();
    }
}
