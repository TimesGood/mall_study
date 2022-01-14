package com.example.mall_study.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

/**
 * Swagger2API文档的配置
 * 配置Api文档
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerConfig.class);
    //application.yml定义属性的映射类
    private final SwaggerProperties swaggerProperties;
    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    /**
     * 生成Api文档界面的配置
     * @return
     */
    @Bean
    public Docket createRestApi(){
        LOGGER.info("接口地址：{}","http://localhost:8080/swagger-ui/index.html");
        return new Docket(DocumentationType.OAS_30).pathMapping("/")//pathMapping接口前缀
                .enable(swaggerProperties.getEnable())
                //页面详细
                .apiInfo(apiInfo())
                //接口调试地址
                .host(swaggerProperties.getTryHost())
                //为每组Api添加描述
                .tags(new Tag("UmsAdminController","后台用户管理"),getTags())
                //选择哪些接口作为doc发布
                .select()
                //为当前包下controller生成API文档
                .apis(RequestHandlerSelectors.basePackage("com.example.mall_study.controller"))
                //为有@Api注解的Controller生成API文档
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //为有@ApiOperation注解的方法生成API文档
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //扫描路径，对所有目录扫描
                .paths(PathSelectors.any())
                .build()
                //支持的通讯协议
                .protocols(newHashSet("https", "http"))
                //授权信息设置，必要的header token等认证信息
                .securitySchemes(securitySchemes())
                //授权信息全局应用
                .securityContexts(securityContexts());
    }

    /**
     * 设置每组Api的描述信息
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
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getApplicationName())
                .description(swaggerProperties.getApplicationDescription())
                .termsOfServiceUrl("对外地址：xxx")
                .contact(new Contact("张文科",null,"2907520924@qq.com"))
                .version(swaggerProperties.getApplicationVersion())
                .build();
    }

    /**
     * 设置授权信息
     * 设置请求头，当一个请求发出时，需要在请求头写携带Authorization这个来确认可以访问
     * @return
     */
    private List<SecurityScheme> securitySchemes() {
        //name：jwt参数名，也是储存token的key，keyname:与name保持一致，passAs存放位置
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
        return Collections.singletonList(apiKey);
    }

    /**
     * 授权信息全局应用
     * 设置需要token信息的接口
     * @return
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder().
                        securityReferences(defaultAuth())
//                        .forPaths(PathSelectors.regex("/brand/.*"))
                        .build());

    }


    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything")
        };
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }
    @SafeVarargs
    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return null;
    }
}
