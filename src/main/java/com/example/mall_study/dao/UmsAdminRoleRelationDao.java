package com.example.mall_study.dao;

import com.example.mall_study.mbg.model.UmsPermission;
import com.example.mall_study.mbg.model.UmsRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UmsAdminRoleRelationDao {

    /**
     * 获取用户所有权限(包括+-权限)
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);
    List<UmsPermission> getPermissionListByUri(@Param("uri") String uri);

    List<UmsRole> getRoleList(@Param("adminId") Long adminId);
}