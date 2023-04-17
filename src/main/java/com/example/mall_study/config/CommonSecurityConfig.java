package com.example.mall_study.config;

import com.example.mall_study.component.dynamicSecurity.DynamicAccessDecisionManager;

import com.example.mall_study.component.dynamicSecurity.DynamicSecurityFilter;
import com.example.mall_study.component.dynamicSecurity.DynamicSecurityMetadataSource;
import com.example.mall_study.component.filter.CheckCodeFilter;

import com.example.mall_study.component.filter.JwtAuthenticationTokenFilter;
import com.example.mall_study.component.filter.JwtUsernamePasswordAuthenticationFilter;
import com.example.mall_study.component.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class CommonSecurityConfig {
    @Autowired
    public AuthenticationConfiguration authenticationConfiguration;

    /**注入PasswordEncoder加密**/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**验证码验证**/
//    @Bean
//    public CheckCodeFilter captchaFilter() {
//        return new CheckCodeFilter();
//    }
    /**
     * 在用户名与密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        return new JwtAuthenticationTokenFilter(authenticationConfiguration.getAuthenticationManager());
    }
    /**访问白名单**/
    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig(){
        return new IgnoreUrlsConfig();
    }
    /**权限不足处理**/
    @Bean
    public JwtAccessDeniedHandler accessDeniedHandler(){
        return new JwtAccessDeniedHandler();
    }
    /**未登录或token过期处理**/
    @Bean
    public JwtAuthenticationEntryPoint authenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint();
    }
    /**登出成功处理**/
    @Bean
    public JwtLogoutSuccessHandler logoutSuccessHandler(){
        return new JwtLogoutSuccessHandler();
    }
    /**登录成功处理**/
    @Bean
    public JwtLoginSuccessHandler loginSuccessHandler(){
        return new JwtLoginSuccessHandler();
    }
    /**登录失败处理**/
    @Bean
    public JwtLoginFailureHandler loginFailureHandler(){
        return new JwtLoginFailureHandler();
    }
    /**
     * 自定义用户名与密码登录校验
     */
    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        return new JwtUsernamePasswordAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(),loginSuccessHandler(),loginFailureHandler());
    }
    /**会话处理，账号被挤下线**/
    @Bean
    public JwtSessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
        return new JwtSessionInformationExpiredStrategy();
    }
    /**动态路径权限管理，权限验证**/
    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager(){
        return new DynamicAccessDecisionManager();
    }
    /**动态资源权限获取源**/
    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource(){
        return new DynamicSecurityMetadataSource();
    }
    /**动态路径权限拦截**/
    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter(){
        return new DynamicSecurityFilter();
    }
}
