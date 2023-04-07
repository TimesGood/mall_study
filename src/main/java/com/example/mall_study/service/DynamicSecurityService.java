package com.example.mall_study.service;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * 基于路径动态权限业务
 */
public interface DynamicSecurityService {
    Map<String,ConfigAttribute> loadDataSource();
}
