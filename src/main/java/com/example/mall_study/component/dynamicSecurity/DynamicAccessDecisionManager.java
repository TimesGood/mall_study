package com.example.mall_study.component.dynamicSecurity;

import com.example.mall_study.common.api.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 动态权限决策管理类，用于判断用户访问的路径是否有权限
 */
public class DynamicAccessDecisionManager implements AccessDecisionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //该用户所拥有资源与访问该路径所配资源比对，如果有该资源的访问，通过教养
        for(ConfigAttribute configAttribute : configAttributes){
            String needAuthority = configAttribute.getAttribute();
            //当该路径并没有配置任何资源时，拒绝访问
            if(needAuthority.equals(ResultCode.FORBIDDEN.getMessage())){
                throw new AccessDeniedException(ResultCode.FORBIDDEN.getMessage());
            }
            //当该路径配置的资源与用户所拥有资源匹配时，通过校验
            for(GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if(needAuthority.trim().equals(grantedAuthority.getAuthority())) return;
            }
        }
        throw new AccessDeniedException(ResultCode.FORBIDDEN.getMessage());
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
