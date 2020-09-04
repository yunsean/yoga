package com.yoga.admin.shiro.freemarker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class ShiroTagsConfigurer {

    @Bean
    public FreeMarkerConfigurationFactoryBean setupFreemarkShiro(FreeMarkerConfigurer configurer) {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        configurer.getConfiguration().setSharedVariable("shiro", new ShiroTags());
        configurer.setDefaultEncoding("UTF-8");
        return bean;
    }
}
