package com.fang.bigdata.springcloudbasicpkg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 // 启用Swagger2
public class Swagger2 {

    //    @Value("${spring.profiles.active}")
//    String ProAction;
    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.fang.bigdata.springcloudbasicpkg"))
                .paths(input -> {
//                    if (ProAction.toLowerCase().equals("prod")){
//                        return false;
//                    }else{
                    return true;
//                    }
                })
                .build();

        return docket;
    }

    @SuppressWarnings("deprecation")
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("数据统计微服务手册", "此在线API手册为调用微服务技术人员提供开发参考", "1.0", "Terms of service", "liulei.bj@fang.com",
                "房天下", "http://www.fang.com/");
        return apiInfo;
    }


}
