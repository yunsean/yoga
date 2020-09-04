package com.yoga.admin;

import com.yoga.core.CoreApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.yoga.**.mapper")
public class AdminApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(
                CoreApplication.class,
                AdminApplication.class
        )
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}