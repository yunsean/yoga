package com.yoga;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
public class UserApplication {

    public static void main(String[] args) {
    	Object [] objects = new Object[]{
                com.yoga.core.CoreApplication.class,
                com.yoga.resource.ResourceApplication.class,
                UserApplication.class};
        new SpringApplicationBuilder(objects).web(true).run(args);
    }

}