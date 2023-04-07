package com.example.mall_study.config;

import com.example.mall_study.component.dynamicSecurity.DynamicAccessDecisionManager;
import com.example.mall_study.component.dynamicSecurity.DynamicSecurityFilter;
import com.example.mall_study.component.dynamicSecurity.DynamicSecurityMetadataSource;
import com.example.mall_study.component.filter.CheckCodeFilter;
import com.example.mall_study.mbg.model.UmsResource;
import com.example.mall_study.service.DynamicSecurityService;
import com.example.mall_study.service.UmsAdminService;
import com.example.mall_study.service.UmsResourceService;
import com.example.mall_study.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CommonSecurityConfig {
    @Autowired
    private UmsResourceService resourceService;

    @Autowired
    private UmsAdminService adminService;
    /**用户登录处理**/
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> adminService.loadUserByUsername(username);
    }
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
    @Bean
    public DynamicSecurityService dynamicSecurityService(){
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                Map<String,ConfigAttribute> map = new ConcurrentHashMap<>();
                List<UmsResource> resourceList = resourceService.listAll();
                for(UmsResource res : resourceList){
                    map.put(res.getUrl(),new org.springframework.security.access.SecurityConfig(res.getId()+":"+res.getName()));
                }
                return map;
            }
        };
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
