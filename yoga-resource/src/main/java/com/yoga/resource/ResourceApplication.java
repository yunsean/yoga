package com.yoga.resource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ResourceApplication {

	public static void main(String[] args) {
		Object [] objects = new Object[]{ResourceApplication.class};
		new SpringApplicationBuilder(objects).web(true).run(args);
	}
}
