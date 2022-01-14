package com.example.mall_study.component;

import com.example.mall_study.common.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 * 在每次进行请求时携带token进行一次免密登录的操作
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    /**在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。**/
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //拿到Authorization头的值
        String authHeader = request.getHeader(this.tokenHeader);
        //当存在token时，并且token前缀与自己设置的前缀相同时（前缀自己在application.yml中自定义jwt属性设置）
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            //截取前缀拿到真正的token
            String authToken = authHeader.substring(this.tokenHead.length());
            //解析token，从token中获取登录用户信息
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            LOGGER.info("解析token用户:{}", username);
            //如果token解析的用户存在，并且之前并没有进行登录操作
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //根据用户名获取该用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //验证该token与数据库中用户信息比对
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    //比对通过，进行登录操作
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("认证通过用户:{}", username);
                    //证明通行
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}