package com.yoga.excelkit;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ExcelkitApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ExcelkitApplication.class).web(WebApplicationType.SERVLET).run(args);
	}
}
