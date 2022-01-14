package com.example.mall_study.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.example.mall_study.mbg.mapper","com.example.mall_study.dao"})//指定mapper配置扫描路径
public class MyBatisConfig {
    /**
     * 注册SqlSessionFactory
     */
//    @Bean
//    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);// 指定数据源(这个必须有，否则报错)
//        factoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));//mybatis-config全局配置文件
//        factoryBean.setTypeAliasesPackage("com.example.mall_study.dao");//指定实体类的包
//        factoryBean.setMapperLocations(new Resource[]{new ClassPathResource("classpath:dao/mapper/*.xml")});
//        return factoryBean;
//    }

}
