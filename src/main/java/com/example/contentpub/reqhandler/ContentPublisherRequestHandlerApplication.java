package com.example.contentpub.reqhandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class ContentPublisherRequestHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentPublisherRequestHandlerApplication.class, args);
	}

}
