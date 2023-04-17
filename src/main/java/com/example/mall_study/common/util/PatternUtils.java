package com.example.mall_study.common.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具
 * @author 张文科
 */
public class PatternUtils {
    private PatternUtils(){}
    /**
     * 正则表达式，判断是否为整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?\\d+$");
        return pattern.matcher(str).matches();
    }
    /**
     * 正则表达式，验证是否为日期
     * @return
     */
    public static boolean isDateTime(String dateStr,String sep) {
        Pattern p=Pattern.compile("(\\d{4})"+sep+"(\\d{1,2})"+sep+"(\\d{1,2})");
        Matcher m=p.matcher(dateStr);
        return m.find();
    }

    /**
     * 将驼峰转为下划线demoTest==>demo_test
     */
    public static String humpToLine(String str) {
        Pattern compile = Pattern.compile("[A-Z]");//匹配大写字母
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb,  "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);//把最后的内容追加到sb
        return sb.toString();
    }

    /**
     * 将下划线转驼峰demo_test==>demoTest
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Pattern compile = Pattern.compile("_[a-z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb,  matcher.group(0).toUpperCase().replace("_",""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母大写
     * @return
     */
    public static String toPrefixCase(String str){
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

    /**
     * 把单独或连续的目标字符串替换成指定字符
     * @param str
     * @return
     */
    public static String replace(String str,String target,String spec){
        Pattern pattern = Pattern.compile("("+target+"+)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        //用正则表达式的find函数去判断，有没有匹配的结果集
        while (matcher.find()) {
            matcher.appendReplacement(sb, spec);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 匹配是否含有中文、英文字母
     */
    public static boolean isChAndEn(String str){
        if(StringUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5a-zA-Z]+");
        return pattern.matcher(str).find();
    }


    /**
     * 替换字符串开始第几个
     */
    public static String replaceFirst(String str,String target,String spec){
        Pattern compile = Pattern.compile("^"+target);
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb,spec);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}