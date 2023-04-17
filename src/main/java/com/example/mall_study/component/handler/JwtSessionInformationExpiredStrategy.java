package com.example.mall_study.component.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.mall_study.common.api.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 会话失效(账号被挤下线)处理逻辑
 */
public class JwtSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtSessionInformationExpiredStrategy.class);
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        LOGGER.info("会话失效处理");
        HttpServletResponse response = event.getResponse();
        response.getWriter().print(JSONObject.toJSON(CommonResult.failed("您在另一处已登录")));
        response.getWriter().flush();
    }
}
