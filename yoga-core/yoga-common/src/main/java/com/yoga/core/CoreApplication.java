package com.yoga.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

@SpringBootApplication
public class CoreApplication {
	private static Logger logger = LoggerFactory.getLogger(CoreApplication.class);

	private static String getDefaultCharSet() {
		OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
		String enc = writer.getEncoding();
		return enc;
	}
	public static void main(String[] args) {
		logger.warn("Default Charset=" + Charset.defaultCharset());
		logger.warn("file.encoding=" + System.getProperty("file.encoding"));
		logger.warn("Default Charset=" + Charset.defaultCharset());
		logger.warn("Default Charset in Use=" + getDefaultCharSet());
		SpringApplication.run(CoreApplication.class, args);
	}
}