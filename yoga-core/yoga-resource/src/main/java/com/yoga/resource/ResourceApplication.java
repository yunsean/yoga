package com.yoga.resource;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ResourceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ResourceApplication.class).web(WebApplicationType.SERVLET).run(args);
	}
}
