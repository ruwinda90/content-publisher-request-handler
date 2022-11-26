package com.example.contentpub.reqhandler.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestConfig {

    @Value("${rest-template.timeout.read}")
    private int readTimeout;
    @Value("${rest-template.timeout.connection}")
    private Integer connectionTimeout;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory simpleClientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory ();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectionTimeout);
        return factory;
    }


}

