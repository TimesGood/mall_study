//package com.example.mall_study.component;
//
//import com.example.mall_study.dao.UmsAdminRoleRelationDao;
//import com.example.mall_study.mbg.model.UmsPermission;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 安全元数据
// */
//@Component
//public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
//    @Autowired
//    UmsAdminRoleRelationDao umsAdminRoleRelationDao;
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
//        //获取请求地址
//        String requestUrl = ((FilterInvocation) o).getRequestUrl();
//        System.out.println("请求地址*********"+requestUrl);
//        //查询具体某个接口需要的权限
//        List<UmsPermission> permissionList =  umsAdminRoleRelationDao.getPermissionListByUri(requestUrl);
//        System.out.println("根据请求地址查询出的权限*********"+permissionList);
//        if(permissionList != null && permissionList.size() != 0){
//            //有权限，访问特定内容
//            String[] attributes = new String[permissionList.size()];
//            for (int i = 0, j = permissionList.size(); i < j; i++) {
//                attributes[i] = permissionList.get(i).getValue();
//            }
//            return SecurityConfig.createList(attributes);
//        }
//        //没有权限，设定一个特定的权限，可以访问所有内容
//        return SecurityConfig.createList("*");
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return true;
//    }
//}