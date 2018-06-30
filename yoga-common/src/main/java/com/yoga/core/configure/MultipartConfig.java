package com.yoga.core.configure;

import com.yoga.core.property.PropertiesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * Created by dylan on 2018/3/16.
 */
@Configuration
public class MultipartConfig {
    @Autowired
    private PropertiesService propertiesService;

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String tmpPath = propertiesService.getFileTempPath();
        factory.setLocation(tmpPath);
        return factory.createMultipartConfig();
    }
}
