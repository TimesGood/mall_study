package com.example.mall_study.service.impl;

import com.example.mall_study.mbg.model.UmsResource;
import com.example.mall_study.service.DynamicSecurityService;
import com.example.mall_study.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("dynamicSecurityService")
public class DynamicSecurityServiceImpl implements DynamicSecurityService {
    @Autowired
    UmsResourceService resourceService;

    @Override
    public Map<String, ConfigAttribute> loadDataSource() {
        Map<String,ConfigAttribute> map = new ConcurrentHashMap<>();
        List<UmsResource> resourceList = resourceService.listAll();
        for(UmsResource res : resourceList){
            map.put(res.getUrl(),new org.springframework.security.access.SecurityConfig(res.getId()+":"+res.getName()));
        }
        return map;
    }
}
