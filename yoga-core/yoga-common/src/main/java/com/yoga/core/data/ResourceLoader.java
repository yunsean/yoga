package com.yoga.core.data;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

public class ResourceLoader {
	
	public static Resource [] getResources(String pathPattern){
    	ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = resourcePatternResolver.getResources(pathPattern);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return resources;
	}
	
	public static Resource getResource(String pathPattern){
    	ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		return resourcePatternResolver.getResource(pathPattern);
	}
}
