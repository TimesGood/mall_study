package com.example.mall_study.service.impl;

import com.example.mall_study.dto.AdminUserDetails;
import com.example.mall_study.mbg.model.UmsAdmin;
import com.example.mall_study.mbg.model.UmsPermission;
import com.example.mall_study.mbg.model.UmsResource;
import com.example.mall_study.mbg.model.UmsRole;
import com.example.mall_study.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UmsAdminService adminService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("请输入用户名");
        }
        //根据用户名获取用户
        UmsAdmin admin = adminService.getAdminByUsername(username);
        if (admin != null) {
            //根据用户Id获取该用户所拥有的权限
            List<UmsPermission> permissionList = adminService.getPermissionList(admin.getId());
            List<UmsRole> roleList = adminService.getRoleList(admin.getId());
            List<UmsResource> resourceList = adminService.getResourceList(admin.getId());
            //返回用户的信息及其该用户的权限列表
            return new AdminUserDetails(admin,roleList,permissionList,resourceList);
        }
        //message消息
        throw new UsernameNotFoundException("用户不存在");
    }
}
