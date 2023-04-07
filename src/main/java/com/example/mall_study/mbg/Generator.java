package com.example.mall_study.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-generator代码生成器
 * 可根据数据库中某个表自动生成mapper接口，mapper.xml、dao实体类、Example
 * 用于生产MBG的代码
 */
public class Generator {
    public static void main(String[] args) throws Exception {
        GeneratorUtil.build();
    }
}