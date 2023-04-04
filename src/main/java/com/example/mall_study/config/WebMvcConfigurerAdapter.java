package com.example.mall_study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


/**
 */
@Configuration
public class WebMvcConfigurerAdapter implements WebMvcConfigurer {
    /**
     * 拦截器配置
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        try {
//            Field registrationsField = FieldUtils.getField(InterceptorRegistry.class, "registrations", true);
//            List<InterceptorRegistration> registrations = (List<InterceptorRegistration>) ReflectionUtils.getField(registrationsField, registry);
//            if (registrations != null) {
//                for (InterceptorRegistration interceptorRegistration : registrations) {
//                    interceptorRegistration
//                            .excludePathPatterns("/swagger**/**")
//                            .excludePathPatterns("/webjars/**")
//                            .excludePathPatterns("/v3/**")
//                            .excludePathPatterns("/doc.html");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 视图跳转
     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/toLogin").setViewName("index.html");
//    }

    /**
     * 静态资源处理
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler对外暴露的路径
        //addResourceLocations服务器内部的资源路径
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources");
//        registry.addResourceHandler("webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/view");
//    }
}
