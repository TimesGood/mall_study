//package com.example.mall_study.component;
//
//import org.springframework.security.access.AccessDecisionManager;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.authentication.InsufficientAuthenticationException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// * 访问决策管理器
// */
//@Component
//public class CustomizeAccessDecisionManager implements AccessDecisionManager {
//    @Override
//    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
//        Iterator<ConfigAttribute> iterator = collection.iterator();
//        while (iterator.hasNext()) {
//            ConfigAttribute attribute = iterator.next();
//            //当前请求需要的权限
//            String permission = attribute.getAttribute();
//            //特定权限可以访问
//            if ("*".equals(permission)) {
//                return;
//            }
//            //当前用户所具有的权限
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//            for (GrantedAuthority authority : authorities) {
//                if (authority.getAuthority().equals(permission)) {
//                    return;
//                }
//            }
//
//        }
//        throw new AccessDeniedException("权限不足!");
//    }
//
//
//    @Override
//    public boolean supports(ConfigAttribute configAttribute) {
//        return true;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return true;
//    }
//}