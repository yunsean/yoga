package com.yoga.content;

import com.yoga.resource.ResourceApplication;
import com.yoga.core.CoreApplication;
import com.yoga.UserApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ContentApplication {

	public static void main(String[] args) {
		Object [] objects = new Object[]{CoreApplication.class,
				ResourceApplication.class,
				UserApplication.class,
				ContentApplication.class};
		new SpringApplicationBuilder(objects).web(true).run(args);
	}
}
