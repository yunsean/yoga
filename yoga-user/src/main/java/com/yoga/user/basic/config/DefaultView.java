package com.yoga.user.basic.config;

import com.yoga.core.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DefaultView extends WebMvcConfigurerAdapter{
	Logger logger = LoggerFactory.getLogger(DefaultView.class);
    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers( registry );
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	Resource [] resources = ResourceLoader.getResources("classpath*:/static");
    	if(null == resources || resources.length == 0){
    		return;
    	}
		Set<String> resourceLocations = new HashSet<>();
		for(Resource resource : resources){
			try {
				String directory = URLDecoder.decode(resource.getFile().getAbsolutePath(), "utf-8");
				directory = "file:" + directory.replaceAll("\\\\", "/") + "/";
				String staticReource = directory;
				logger.info("加载静态资源目录：" + staticReource);
				resourceLocations.add(staticReource);
			} catch (UnsupportedEncodingException e) {
				logger.warn("静态资源加载失败...");
			} catch (IOException e) {
				logger.warn("未找到静态资源文件");
			}
		}
		if(resourceLocations.size() == 0){
			return;
		}
        registry.addResourceHandler("/**")
                .addResourceLocations(resourceLocations.toArray(new String[resourceLocations.size()]));
    }
}