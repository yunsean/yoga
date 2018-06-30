package com.yoga.core.utils;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

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
