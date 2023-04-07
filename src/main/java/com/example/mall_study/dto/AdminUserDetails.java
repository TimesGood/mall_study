package com.example.mall_study.dto;

import com.example.mall_study.mbg.model.UmsAdmin;
import com.example.mall_study.mbg.model.UmsPermission;
import com.example.mall_study.mbg.model.UmsResource;
import com.example.mall_study.mbg.model.UmsRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 */
public class AdminUserDetails implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";
    private UmsAdmin umsAdmin;
    private List<UmsPermission> permissionList;
    private List<UmsRole> roleList;
    private List<UmsResource> resourceList;

    public AdminUserDetails(UmsAdmin umsAdmin, List<UmsRole> roleList,List<UmsPermission> permissionList,List<UmsResource> resourceList) {
        this.umsAdmin = umsAdmin;
        this.roleList = roleList;
        this.permissionList = permissionList;
        this.resourceList = resourceList;
    }

    /**
     * 指定校验权限的字段，权限最终交由AuthenticationManager管理
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return resourceList.stream()
                .map(role ->new SimpleGrantedAuthority(role.getId()+":"+role.getName()))
                .collect(Collectors.toList());
        //权限
//        List<SimpleGrantedAuthority> permissions = permissionList.stream()//将集合转为流
//                //过滤空元素
//                .filter(permission -> permission.getValue() != null)
//                //用于映射每个元素到对应的结果,就是把List<UmsPermission>替换为List<GrantedAuthority>
//                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
//                .collect(Collectors.toList());//将流转为集合
//        //角色
//        List<SimpleGrantedAuthority> roles = roleList.stream()
//                .filter(role -> role.getName() != null)
//                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX+role.getName()))
//                .collect(Collectors.toList());
//        //资源
//        List<SimpleGrantedAuthority> resource = resourceList.stream()
//                .filter(res -> res.getName() != null)
//                .map(res -> new SimpleGrantedAuthority(res.getId()+":"+res.getName()))
//                .collect(Collectors.toList());
//        List<SimpleGrantedAuthority> collect = new ArrayList<>();
//        collect.addAll(permissions);
//        collect.addAll(roles);
//        collect.addAll(resource);
//        return collect;
    }


    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return umsAdmin.getStatus().equals(1);
    }
}
