package com.yoga.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * 参数加载
 * @author Skysea
 *
 */
public class PropertiesLoader {
	
	public static final Logger logger = Logger.getLogger(PropertiesLoader.class);
	public static Properties loadPropertiesByClasspath(String classpath){
		InputStream in = ClassLoader.getSystemResourceAsStream(classpath);
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return properties;
	}
	
	
	public static Properties loadPropertiesByRootPath(String fileName){
		File file = new File(fileName);
		Properties properties = new Properties();
		try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return properties;
	}
}
