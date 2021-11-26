package com.example.mall_study.service;


import com.example.mall_study.common.api.CommonResult;

/**
 * 会员管理Service
 */
public interface UmsMemberService {

    /**
     * 生成验证码
     */
    CommonResult<String> generateAuthCode(String telephone);

    /**
     * 判断验证码和手机号码是否匹配
     */
    CommonResult<String> verifyAuthCode(String telephone, String authCode);

}
