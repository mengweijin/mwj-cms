package com.mwj.cms.framework.swagger;

import com.mwj.cms.framework.AppSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Meng Wei Jin
 * @description http://localhost/swagger-ui.html
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private AppSupport appSupport;

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mwj.cms"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("mwj-cms后台管理系统_接口文档")
                .description("mwj-cms后台管理系统")
                //.termsOfServiceUrl("服务条款")
                .contact(new Contact(appSupport.getAuthor(), appSupport.getBlog(), appSupport.getMail()))
                .version(appSupport.getVersion())
                .build();
    }
}
