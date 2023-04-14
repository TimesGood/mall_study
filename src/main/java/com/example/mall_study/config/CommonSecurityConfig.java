package com.example.mall_study.config;

import com.example.mall_study.component.dynamicSecurity.DynamicAccessDecisionManager;
import com.example.mall_study.component.dynamicSecurity.DynamicSecurityFilter;
import com.example.mall_study.component.dynamicSecurity.DynamicSecurityMetadataSource;
import com.example.mall_study.component.filter.CheckCodeFilter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class CommonSecurityConfig {
    /**注入PasswordEncoder加密**/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**验证码验证**/
    @Bean
    public CheckCodeFilter captchaFilter() {
        return new CheckCodeFilter();
    }
    /**访问白名单**/
    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig(){
        return new IgnoreUrlsConfig();
    }
    /**动态路径权限拦截**/
    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter(){
        return new DynamicSecurityFilter();
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

}
