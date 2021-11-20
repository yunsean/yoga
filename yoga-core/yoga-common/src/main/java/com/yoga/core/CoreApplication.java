package com.yoga.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

@Slf4j
@SpringBootApplication
public class CoreApplication {
	private static String getDefaultCharSet() {
		OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
		String enc = writer.getEncoding();
		return enc;
	}
	public static void main(String[] args) {
		log.warn("Default Charset=" + Charset.defaultCharset());
		log.warn("file.encoding=" + System.getProperty("file.encoding"));
		log.warn("Default Charset=" + Charset.defaultCharset());
		log.warn("Default Charset in Use=" + getDefaultCharSet());
		SpringApplication.run(CoreApplication.class, args);
	}
}