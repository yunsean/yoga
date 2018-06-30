package com.yoga.imessager;

import com.yoga.resource.ResourceApplication;
import com.yoga.core.CoreApplication;
import com.yoga.UserApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@MapperScan("com.yoga.**.mapper")
public class IMessagerApplication {

	public static void main(String[] args) {
		Object[] objects = new Object[]{
				CoreApplication.class,
				ResourceApplication.class,
				UserApplication.class,
				IMessagerApplication.class};
		new SpringApplicationBuilder(objects).web(true).run(args);
	}
}
