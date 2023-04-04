package com.example.mall_study.controller;

import com.example.mall_study.common.api.CommonResult;
import com.example.mall_study.common.util.RedisUtil;
import com.example.mall_study.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 会员登录注册管理Controller
 */
@Controller
@Api(tags = {"会员登录注册管理"})
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
    @ApiOperation("获取验证码图片")
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> getCode(HttpServletRequest request, HttpServletResponse response){
        //生成图片宽度
        int width = 130;
        int height = 45;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);//验证码背景色
        g.fillRect(0,0,width,height);
        Random random = new Random();
        String checkCode = getCheckCode();
        //把验证码放到session中
        request.getSession().setAttribute("checkCode_session",checkCode);
        Color color = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
        g.setColor(color);
        g.setFont(new Font("微软雅黑",Font.BOLD,40));
        //向图片写入验证码
        g.drawString(checkCode,15,33);
        //画干扰线
        for(int i = 0;i < 6;i++){
            Color line = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            g.setColor(line);
            g.drawLine(random.nextInt(width), random.nextInt(height),random.nextInt(width), random.nextInt(height));
        }
        //添加图块噪点
        for(int i = 0;i < 30;i++){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Color rect = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            g.setColor(rect);
            g.fillRect(x,y,2,2);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image,"png",stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String codeImg = Base64Utils.encodeToString(stream.toByteArray());
        return CommonResult.success(codeImg);
    }

    /**
     * 生成四位验证码
     * @return
     */
    public String getCheckCode(){
        String base = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int size = base.length();
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i < 4;i++){
            //产生随机数
            int index = random.nextInt(size);
            char c = base.charAt(index);
            sb.append(c);
        }
        return sb.toString();
    }
}
