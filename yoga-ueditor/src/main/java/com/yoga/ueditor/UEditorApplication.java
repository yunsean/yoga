package com.yoga.ueditor;

import com.yoga.core.CoreApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UEditorApplication {

	public static void main(String[] args) {
		Object [] objects = new Object[]{
				CoreApplication.class,
				UEditorApplication.class};
		new SpringApplicationBuilder(objects).web(true).run(args);
	}
}
