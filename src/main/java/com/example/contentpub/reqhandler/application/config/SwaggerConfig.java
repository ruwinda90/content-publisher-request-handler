package com.example.contentpub.reqhandler.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {

    @Value("${app.host}")
    private String host;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .select().apis(RequestHandlerSelectors.basePackage("com.example.contentpub.reqhandler.application.controller"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(apiInfo()).pathProvider(new PathProvider() {
                    @Override
                    public String getOperationPath(String operationPath) {
                        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
                        return Paths.removeAdjacentForwardSlashes(
                                uriComponentsBuilder.path(operationPath).build().toString());
                    }

                    @Override
                    public String getResourceListingPath(String groupName, String apiDeclaration) {
                        return null;
                    }
                });
    }

    private ApiInfo apiInfo() {
        String title ="Content Publisher Platform - Request Handler";
        return new ApiInfo(
                title,
                "Content publisher platform",
                "v1.0",
                "Terms of services",
                new Contact("Admin", "http://localhost:8080", "john.doe@sample.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

}
