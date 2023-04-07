package com.example.mall_study.service.impl;


import com.example.mall_study.common.util.JwtTokenUtil;
import com.example.mall_study.dao.UmsAdminRoleRelationDao;
import com.example.mall_study.dto.AdminUserDetails;
import com.example.mall_study.mbg.mapper.UmsAdminMapper;
import com.example.mall_study.mbg.model.*;
import com.example.mall_study.service.UmsAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * UmsAdminService实现类
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdmin umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectByExample(example);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            //根据用户名获取用户
            UserDetails userDetails = loadUserByUsername(username);
            //输入的密码与用户的密码比对
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            //验证成功把用户的信息最重要的就是权限，userDetails.getAuthorities()把用户所持权限拿出交由AuthenticationManager进行管理
            //之后再controller通过注解@PreAuthorize("hasAuthority('pms:brand:read')")，单引号就是需要的权限，访问该接口时会将此注解中字符串
            //与被管理的用户的权限列表比对，有就可以继续访问，没有则抛AccessDeniedException异常
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //制作token
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public String logout() {

        return null;
    }

    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {
        return adminRoleRelationDao.getPermissionList(adminId);
    }

    @Override
    public List<UmsPermission> getPermissionListByUri(String uri) {
        return adminRoleRelationDao.getPermissionListByUri(uri);
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        return adminRoleRelationDao.getResourceList(adminId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("请输入用户名");
        }
        //根据用户名获取用户
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            //根据用户Id获取该用户所拥有的权限
            List<UmsPermission> permissionList = getPermissionList(admin.getId());
            List<UmsRole> roleList = getRoleList(admin.getId());
            List<UmsResource> resourceList = getResourceList(admin.getId());
            //返回用户的信息及其该用户的权限列表
            return new AdminUserDetails(admin,roleList,permissionList,resourceList);
        }
        //message消息
        throw new UsernameNotFoundException("用户不存在");
    }
}
