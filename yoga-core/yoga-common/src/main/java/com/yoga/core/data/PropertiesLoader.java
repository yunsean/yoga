package com.yoga.core.data;

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
	public static Properties loadPropertiesByClasspath(String classpath){
		InputStream in = ClassLoader.getSystemResourceAsStream(classpath);
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return properties;
	}
}
