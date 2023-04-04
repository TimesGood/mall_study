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
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity的配置
 * 配置登录：
 * 首先我们在配置的时候，defaultSuccessUrl 和 successForwardUrl 只需要配置一个即可，具体配置哪个，则要看你的需求，两个的区别如下：
 *  -defaultSuccessUrl 有一个重载的方法，我们先说一个参数的 defaultSuccessUrl 方法。如果我们在 defaultSuccessUrl
 *  中指定登录成功的跳转页面为 /index，此时分两种情况，如果你是直接在浏览器中输入的登录地址，登录成功后，就直接跳转到 /index，
 *  如果你是在浏览器中输入了其他地址，例如 http://localhost:8080/hello，结果因为没有登录，又重定向到登录页面，
 *  此时登录成功后，就不会来到 /index ，而是来到 /hello 页面。
 *  -defaultSuccessUrl 还有一个重载的方法，第二个参数如果不设置默认为 false，也就是我们上面的的情况，如果手动设置第二个参数为 true，
 *  则 defaultSuccessUrl 的效果和 successForwardUrl 一致。
 *  -successForwardUrl 表示不管你是从哪里来的，登录后一律跳转到 successForwardUrl 指定的地址。例如 successForwardUrl
 *  指定的地址为 /index ，你在浏览器地址栏输入 http://localhost:8080/hello，结果因为没有登录，重定向到登录页面，当你登录成功之后，就会服务端跳转到 /index 页面；或者你直接就在浏览器输入了登录页面地址，登录成功后也是来到 /index。
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
    @Autowired
    private JwtSessionInformationExpiredStrategy sessionInformationExpiredStrategy;
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
    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter();
        filter.setFilterProcessesUrl("/admin/login");
        filter.setUsernameParameter("username");
        filter.setPasswordParameter("password");
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);//登录成功处理
        filter.setAuthenticationFailureHandler(loginFailureHandler);//登录失败处理
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
    //在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception{
        return new JwtAuthenticationTokenFilter(authenticationManager());
    }
    //验证码验证
    @Bean
    public CheckCodeFilter captchaFilter() {
        return new CheckCodeFilter();
    }

    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig(){
        return new IgnoreUrlsConfig();
    }

    //配置UserDetail和密码以及加密方式
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    //用于配置需要拦截的url路径、jwt过滤器及出异常后的处理器
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();
        //**********************************************配置拦截规则****************************************************
        for (String url:ignoreUrlsConfig().getUrls()){
            registry.antMatchers(url).permitAll();
        }
        //**********************************************登录、登出****************************************************
        registry
                //登录配置（Security只设置了表单登录，如果是前后端分离项目通过Json提交登录，
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
                .logoutSuccessHandler(logoutSuccessHandler)
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
                .accessDeniedHandler(accessDeniedHandler)//权限拒绝处理逻辑
                .authenticationEntryPoint(authenticationEntryPoint)//用户访问无权限资源时的异常处理
                //自定义处过滤器
                //添加Jwt认证，当请求接口需要认证登录时，Jwt将解析token进行一次用户登录
                .and()
//                .addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(usernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);

    }
}