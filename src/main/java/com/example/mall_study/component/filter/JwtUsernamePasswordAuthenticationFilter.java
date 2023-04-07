package com.example.mall_study.component.filter;

import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 用户名密码进行认证安全过滤器
 * 过滤器默认request.getParameter()取得的是表单里面的数据，
 * 而我们通过json提交的用户信息源代码这样就取不到，所以就需要我们
 * 重写attemptAuthentication()方法自己分辨是表单提交还是json
 * 提交，如果是json提交我们在这里就可以取得json数据中的用户信息
 * 解析给认证者拿去认证
 */
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUsernamePasswordAuthenticationFilter.class);
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LOGGER.info("登录操作，如果是json提交的数据，解析json给认证者");
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }// 判断请求是否是json格式，如果不是直接调用父类
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            JSONObject json = null;
            try {
                //获取流，取得流里面的json数据
                BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null){
                    responseStrBuilder.append(inputStr);
                }
                json = new JSONObject(responseStrBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把取得的json对象根据key取得里面的用户名和密码
            UsernamePasswordAuthenticationToken authenticationToken = null;
            if(json !=null) {
                String username = json.getStr(this.getUsernameParameter());
                String password = json.getStr(this.getPasswordParameter());
                if(username == null) username = "";
                if(password == null) password = "";
                username = username.trim();
                //将账号密码组成Authentication
                authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
            }
            this.setDetails(request,authenticationToken);
            //把Authentication交给AuthenticationManager进行认证
            //AuthenticationManager被ProviderManager实现，ProviderManager又把Authentication
            //交给AuthenticationProvider，AuthenticationProvider被DaoAuthenticationProvider实现
            //DaoAuthenticationProvider调用UserDetailsService的loadUserByUsername方法
            //而这个UserDetailsService就是我们要实现的类
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

}
