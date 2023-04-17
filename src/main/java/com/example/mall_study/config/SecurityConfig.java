package com.example.mall_study.config;

import com.example.mall_study.service.DynamicSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity的配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
    @Autowired(required = false)
    DynamicSecurityService dynamicSecurityService;
    @Autowired
    private CommonSecurityConfig config;

    //用于配置需要拦截的url路径、jwt过滤器及出异常后的处理器
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        //**********************************************配置拦截规则****************************************************
        for (String url:config.ignoreUrlsConfig().getUrls()){
            registry.antMatchers(url).permitAll();
        }
        //**********************************************登录、登出****************************************************
        registry
                //登录配置（Security只设置了表单登录，如果是前后端分离项目需通过Json提交登录，
                //需要重写UsernamePasswordAuthenticationFilter，注入容器并配置addFilterAt()，相应的属性也在注入容器的时候配置）
//                .formLogin()
//                .loginProcessingUrl("/admin/login").permitAll()
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .successHandler(loginSuccessHandler)
//                .failureHandler(loginFailureHandler)
                //登出
                .and()
                .logout()
                .logoutUrl("/admin/logout").permitAll()
                .logoutSuccessHandler(config.logoutSuccessHandler())
                .deleteCookies("JSESSIONID");//登出之后删除cookie
        //************************************跨域、异常、认证、过滤器处理*************************************************
        registry.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //异常处理(权限拒绝、登录失效等)
                .and().exceptionHandling()
                .accessDeniedHandler(config.accessDeniedHandler())//权限拒绝处理逻辑
                .authenticationEntryPoint(config.authenticationEntryPoint())//用户访问无权限资源时的异常处理
                //自定义处过滤器
                //添加Jwt认证，当请求接口需要认证登录时，Jwt将解析token进行一次用户登录
                .and()
//                .addFilterBefore(config.captchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(config.jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(config.usernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
        //********************************************基于路径动态权限处理***********************************************
        //当DynamicSecurityService被实现并被注入容器时，开启动态权限控制
        if(dynamicSecurityService != null) {
            registry.and().addFilterBefore(config.dynamicSecurityFilter(), FilterSecurityInterceptor.class);
        }
        return http.build();
    }
}