package com.yoga.core.data;

import com.yoga.core.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 参数加载
 * @author Skysea
 *
 */
public class PropertiesLoader {
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);

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
