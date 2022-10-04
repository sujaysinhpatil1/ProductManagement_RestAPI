package com.jbk.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class ProjectManagementRestApi2Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementRestApi2Application.class, args);
	}
	
	@Bean
	public CommonsMultipartResolver commonsMultipsrtResolver() {
		return new CommonsMultipartResolver();
	}

}
