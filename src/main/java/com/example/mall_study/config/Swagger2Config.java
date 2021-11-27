package com.example.mall_study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2API文档的配置
 * 配置Api文档
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    /**
     * 生成Api文档界面的配置
     * @return
     */
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.OAS_30)
                //页面详细
                .apiInfo(apiInfo())
                .tags(new Tag("UmsAdminController","后台用户管理"),getTags())
                .select()
                //为当前包下controller生成API文档
                .apis(RequestHandlerSelectors.basePackage("com.example.mall_study.controller"))
                //为有@Api注解的Controller生成API文档
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //为有@ApiOperation注解的方法生成API文档
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                //添加登录认证
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    /**
     * 设置每组Api的标题
     * 直接在控制层注解@Api(tags = {"name"})即可
     * @return 标题组
     */
    private Tag[] getTags() {
        return new Tag[]{
                new Tag("UmsMemberController", "会员登录注册管理"),
                new Tag("PmsBrandController", "商品品牌管理"),
                new Tag("EsProductController","搜索商品管理")
        };
    }

    /**
     * 写入Api的一些信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("商城物品管理")
                .description("api根地址：http://localhost:8080/")
                .termsOfServiceUrl("对外地址：xxx")
                .contact(new Contact("张文科","http://localhost:8080/","2907520924@qq.com"))
                .version("1.0")
                .build();
    }

    /**
     * 设置请求头，当一个请求发出时，需要在请求头写携带Authorization这个来确认可以访问
     * @return
     */
    private List<SecurityScheme> securitySchemes() {
        //设置请求头信息
        List<SecurityScheme> schemeList = new ArrayList<>();
        //name：jwt参数名，也是储存token的key，keyname:与name保持一致，passAs存放位置
        SecurityScheme schemes = new ApiKey("Authorization", "Authorization", "header");
        schemeList.add(schemes);
        return schemeList;
    }

    /**
     * 设置需要登录验证Authorization的接口路径
     * @return
     */
    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的接口路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/brand/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex){
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }
}
