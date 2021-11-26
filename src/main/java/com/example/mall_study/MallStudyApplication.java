package com.example.mall_study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MallStudyApplication /*extends SpringBootServletInitializer */{
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        // 注意这里要指向原先用main方法执行的Application启动类
//        return builder.sources(MallStudyApplication.class);
//    }
    public static void main(String[] args) {
        SpringApplication.run(MallStudyApplication.class, args);
    }

}
