package com.example.mall_study.controller;

import com.example.mall_study.common.api.CommonResult;
import com.example.mall_study.config.JwtProperties;
import com.example.mall_study.dto.UmsAdminLoginParam;
import com.example.mall_study.mbg.model.UmsAdmin;
import com.example.mall_study.mbg.model.UmsPermission;
import com.example.mall_study.mbg.model.UmsRole;
import com.example.mall_study.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户管理
 */
@Controller
@Api(tags = {"后台用户管理"})
@RequestMapping("/admin")
public class UmsAdminController {
    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private JwtProperties jwtProperties;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdmin umsAdminParam, BindingResult result) {
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    /**
     * 当在SecurityConfig中配置了登录地址时，如果配置的地址与这里一致时，这里可以不用写了
     * 因为校验登录的逻辑已经被AuthenticationManager管理
     * @param umsAdminLoginParam
     * @param result
     * @return
     */
    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Map<String,String>> login(@RequestBody UmsAdminLoginParam umsAdminLoginParam, BindingResult result) {
        String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenPrefix", jwtProperties.getTokenPrefix());
        return CommonResult.success(tokenMap);
    }
    @ApiOperation(value = "账号登出")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout(){
        adminService.logout();
        return CommonResult.success("登出成功");
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "adminId",value = "用户id" )
    })
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsPermission>> getPermissionList(@RequestParam(value = "adminId") Long adminId) {
        List<UmsPermission> permissionList = adminService.getPermissionList(adminId);
        return CommonResult.success(permissionList);
    }

    @ApiOperation("获取用户角色")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "adminId",value = "用户id" )
    })
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsRole>> getRoleList(@RequestParam(value = "adminId") Long adminId) {
        List<UmsRole> umsRoleList = adminService.getRoleList(adminId);
        return CommonResult.success(umsRoleList);
    }
}
