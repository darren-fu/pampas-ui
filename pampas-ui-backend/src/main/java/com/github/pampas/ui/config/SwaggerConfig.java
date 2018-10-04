package com.github.pampas.ui.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-09-19
 */
@Configuration
public class SwaggerConfig {

    @SuppressWarnings("Guava")
    @Bean
    public Docket docket() {
        String basePackage = "com.github.pampas";
        Predicate<RequestHandler> ownApiSelector = RequestHandlerSelectors.basePackage(basePackage);

        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(ownApiSelector)
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(operationParameters())
                .apiInfo(ApiInfo.DEFAULT);
    }

    private List<Parameter> operationParameters() {
        List<Parameter> operationParameters = new ArrayList();

        operationParameters.add(new ParameterBuilder()
                .name("token")
                .description("请求令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
        return operationParameters;
    }

}
