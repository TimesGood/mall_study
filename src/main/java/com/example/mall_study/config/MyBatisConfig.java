package com.example.mall_study.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.example.mall_study.mbg.mapper","com.example.mall_study.dao"})//指定mapper配置扫描路径
public class MyBatisConfig {
}
