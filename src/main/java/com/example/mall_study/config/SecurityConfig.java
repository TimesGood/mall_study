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
    private JwtAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private JwtLoginSuccessHandler loginSuccessHandler;
    @Autowired
    private JwtLoginFailureHandler loginFailureHandler;
    //访问决策管理器
//    @Autowired
//    CustomizeAccessDecisionManager accessDecisionManager;
//    //实现权限拦截
//    @Autowired
//    CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;
//    @Autowired
//    private CustomizeAbstractSecurityInterceptor securityInterceptor;
    @Autowired
    private JwtSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    //用于配置需要拦截的url路径、jwt过滤器及出异常后的处理器
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //**********************************************登录、异常处理****************************************************
        //csrf禁用，csrf其实就是一个token字符串，前端登录页面的一个隐藏框发送过来的值，解决sessionID被劫持时的非法访问问题，security自动判断csrf是否正确
        httpSecurity.cors().and().csrf().disable()
                //表单登入（Security只设置了表单登录，如果是前后端分离项目需要Json提交登录的），开启之后，原登录接口不可用
                .formLogin()
//                .loginProcessingUrl("/admin/login").permitAll()//指定登录接口，允许所有用户
//                .usernameParameter("username")
//                .passwordParameter("password")
                .successHandler(loginSuccessHandler)//登录成功处理逻辑
                .failureHandler(loginFailureHandler)//登录失败处理逻辑
                //登出，开启之后原登出接口会被拦截
                .and().logout()
//                .logoutUrl("/admin/logout").permitAll()//允许所有用户
                .logoutSuccessHandler(logoutSuccessHandler)//登出成功处理逻辑
                .deleteCookies("JSESSIONID")//登出之后删除cookie
                //会话管理
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//基于token，所以不需要session，STATELESS表示不创建session，这里还有很多状态,ALWAYS表示创建session
//                .maximumSessions(1)//一个账号最大允许登录数量
//                .expiredSessionStrategy(sessionInformationExpiredStrategy)//会话失效(账号被挤下线)处理逻辑
//                .maxSessionsPreventsLogin(true)// 账号已经被其他地方登录了，是否拒绝对方继续登录
                //异常处理(权限拒绝、登录失效等)
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)//权限拒绝处理逻辑
                .authenticationEntryPoint(authenticationEntryPoint)//匿名用户访问无权限资源时的异常处理
                //自定义处过滤器
                //添加Jwt认证，当请求接口需要认证登录时，Jwt将解析token进行一次用户登录
                .and()
                .addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//                .addFilterAt(usernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);


        //**********************************************配置拦截规则****************************************************
        httpSecurity.authorizeRequests()
                //放行,允许不需要验证登录是否直接放行的请求路径
                .antMatchers(HttpMethod.GET, //允许对于网站静态资源的无授权访问
                        "/swagger**/**",
                        "/webjars/**",
                        "/v3/**",
                        "/doc.html",
                        "/index.html",
                        "/esProduct/**",
                        "/sso/*"
                ).permitAll()//允许所有用户
                .antMatchers(HttpMethod.POST,
                        "/admin/login",
                        "/admin/logout",
                        "/sso/*"
                ).permitAll()
                .anyRequest().authenticated()//除上面外的所有请求全部需要鉴权认证
                .and()
                .headers().frameOptions().disable();
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
    //登录身份验证
//    @Bean
//    public CustomizeUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
//        CustomizeUsernamePasswordAuthenticationFilter filter = new CustomizeUsernamePasswordAuthenticationFilter();
//        filter.setFilterProcessesUrl("/admin/login");
//        filter.setUsernameParameter("username");
//        filter.setPasswordParameter("password");
//        filter.setAuthenticationSuccessHandler(loginSuccessHandler);//登录成功处理
//        filter.setAuthenticationFailureHandler(loginFailureHandler);//登录失败处理
//        filter.setAuthenticationManager(authenticationManagerBean());
//        return filter;
//    }
    //在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception{
        return new JwtAuthenticationTokenFilter(authenticationManagerBean());
    }

    @Bean
    public CaptchaFilter captchaFilter() {
        return new CaptchaFilter();
    }
}