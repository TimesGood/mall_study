package com.example.mall_study.controller;

import com.example.mall_study.common.api.CommonResult;
import com.example.mall_study.common.util.RedisUtil;
import com.example.mall_study.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 会员登录注册管理Controller
 */
@Controller
@Api(tags = {"UmsMemberController"})
@RequestMapping("/sso")
public class UmsMemberController {
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam String telephone) {
        return memberService.generateAuthCode(telephone);
    }
    @ApiOperation("获取验证码（RedisTemplate缓存备用）")
    @RequestMapping(value = "/getAuthCodeBack", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCodeBack(@RequestParam String telephone) {
        return memberService.generateAuthCodeBack(telephone);
    }

    @ApiOperation("判断验证码是否正确")
    @RequestMapping(value = "/verifyAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestParam String telephone,
                                 @RequestParam String authCode) {
        return memberService.verifyAuthCode(telephone,authCode);
    }

    @ApiOperation("判断验证码是否正确（RedisTemplate缓存备用）")
    @RequestMapping(value = "/verifyAuthCodeBack", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePasswordBack(@RequestParam String telephone,
                                       @RequestParam String authCode) {
        return memberService.verifyAuthCodeBack(telephone,authCode);
    }
}
