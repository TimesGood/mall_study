package com.example.mall_study.config;

import com.example.mall_study.component.*;
import com.example.mall_study.service.UmsAdminService;
import com.example.mall_study.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity的配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private CustomizeAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private CustomizeLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private CustomizeAuthenticationFailureHandler authenticationFailureHandler;
    //访问决策管理器
//    @Autowired
//    CustomizeAccessDecisionManager accessDecisionManager;
//    //实现权限拦截
//    @Autowired
//    CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;
//    @Autowired
//    private CustomizeAbstractSecurityInterceptor securityInterceptor;
    @Autowired
    private CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    //用于配置需要拦截的url路径、jwt过滤器及出异常后的处理器
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //**********************************************登录、异常处理****************************************************
        //csrf禁用，csrf其实就是一个token字符串，前端登录页面的一个隐藏框发送过来的值，解决sessionID被劫持时的非法访问问题，security自动判断csrf是否正确
        httpSecurity.cors().and().csrf().disable()
                //登出
                .logout().logoutUrl("/admin/logout").permitAll()//允许所有用户
                .logoutSuccessHandler(logoutSuccessHandler)//登出成功处理逻辑
                .deleteCookies("JSESSIONID")//登出之后删除cookie
                //登入
//                .and().formLogin()
//                .loginProcessingUrl("/admin/login").permitAll()//指定登录接口，允许所有用户
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .successHandler(authenticationSuccessHandler)//登录成功处理逻辑
//                .failureHandler(authenticationFailureHandler)//登录失败处理逻辑
                //异常处理(权限拒绝、登录失效等)
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)//权限拒绝处理逻辑
                .authenticationEntryPoint(authenticationEntryPoint)//匿名用户访问无权限资源时的异常处理
                //会话管理
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//基于token，所以不需要session，STATELESS表示不创建session，这里还有很多状态,ALWAYS表示创建session
                // 一个账号最大允许登录数量
                .maximumSessions(1)
                // 账号已经被其他地方登录了，是否允许继续登录 false表示能继续登录，其他地方被挤下线
                .maxSessionsPreventsLogin(true)
                .expiredSessionStrategy(sessionInformationExpiredStrategy);//会话失效(账号被挤下线)处理逻辑

        //**********************************************拦截放行请求****************************************************
        //authorizeRequests拦截所有请求
        httpSecurity.authorizeRequests()
                //放行,允许不需要验证登录是否直接放行的请求路径
                .antMatchers(HttpMethod.GET, //允许对于网站静态资源的无授权访问
                        "/swagger**/**",
                        "/webjars/**",
                        "/v3/**",
                        "/doc.html",
                        "/index.html",
                        "/esProduct/**"
                ).permitAll()//允许所有用户
                .antMatchers(HttpMethod.POST,"/admin/login").permitAll()
                .antMatchers(HttpMethod.POST,"/admin/logout").permitAll()
                .anyRequest().authenticated()//除上面外的所有请求全部需要鉴权认证
                .and()
                .headers().frameOptions().disable();
        //设置登录前拦截，原登录验证需要提交表单，而JSON方式提交不了，设置请求时拦截，如果是json就把
        httpSecurity.addFilterAfter(usernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
        //添加Jwt认证，当请求接口需要认证登录时，Jwt将解析token进行一次用户登录
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    //配置用户名和密码以及加密方式
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    //注入获取用户信息的操作
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
    //注入PasswordEncoder加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //提交数据前，把传递过来的表单数据做json解析
    @Bean
    public CustomizeUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        CustomizeUsernamePasswordAuthenticationFilter filter = new CustomizeUsernamePasswordAuthenticationFilter();
        filter.setFilterProcessesUrl("/admin/login");
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);//登录成功处理
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);//登录失败处理
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
    //在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}