package com.example.mall_study.component.dynamicSecurity;

import com.example.mall_study.component.dynamicSecurity.DynamicAccessDecisionManager;
import com.example.mall_study.component.dynamicSecurity.DynamicSecurityMetadataSource;
import com.example.mall_study.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Security把用户拥有的权限值与接口上定义的的权限值作比对，如果包含则可以访问，反之拒绝。
 * 但这样会产生一个问题，只能挨个控制接口的权限，不能动态批量修改。
 * 解决方案：基于路径动态权限控制
 * 调用super.beforeInvocation(fi)方法时会调用AccessDecisionManager中的decide方法用于鉴权操作，
 * 而decide方法中的configAttributes参数会通过SecurityMetadataSource中的getAttributes方法来获取，
 * configAttributes其实就是配置好的访问当前接口所需要的权限，
 * 注意：开启动态权限控制后，就不要使用注解配置权限了，否则所有某角色已经配置了该资源的访问权限，却还是没有权限访问
 */

public class DynamicSecurityFilter extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    public void setCustomAccessDecisionManager(DynamicAccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        String filterName = getAlreadyFilteredAttributeName();
        boolean isOne = request.getAttribute(filterName) != null;
        if(isOne) {
            fi.getChain().doFilter(fi.getRequest(),fi.getResponse());
            return;
        }
        request.setAttribute(filterName,Boolean.TRUE);
        //跨域请求OPTIONS放行
        if(request.getMethod().equals(HttpMethod.OPTIONS.toString())){
            fi.getChain().doFilter(fi.getRequest(),fi.getResponse());
            return;
        }
        //白名单放行
        PathMatcher pathMatcher = new AntPathMatcher();
        boolean flag = ignoreUrlsConfig.getUrls().stream()
                .anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));
        if(flag) {
            fi.getChain().doFilter(fi.getRequest(),fi.getResponse());
            return;
        }

        //此处AccessDecisionManager中的decide进行鉴权操作
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(),fi.getResponse());
        }finally {
            super.finallyInvocation(token);
        }
        super.afterInvocation(token,(Object) null);
    }
    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public DynamicSecurityMetadataSource obtainSecurityMetadataSource() {
        return dynamicSecurityMetadataSource;
    }

    /**
     * 解决过滤器执行两次的操作
     */
    protected String getAlreadyFilteredAttributeName() {
        return this.getClass().getName() + ".FILTERED";
    }
}