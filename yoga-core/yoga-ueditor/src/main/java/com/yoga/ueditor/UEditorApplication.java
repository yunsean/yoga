package com.yoga.ueditor;

import com.yoga.core.CoreApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UEditorApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(
				CoreApplication.class,
				UEditorApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}
}
