package com.yoga.core.error;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WebSpringMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public GlobalHandlerExceptionResolver globalHandlerExceptionResolver() {
        return new GlobalHandlerExceptionResolver();
    }
}