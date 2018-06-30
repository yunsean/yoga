package com.yoga.ewedding;

import com.yoga.core.CoreApplication;
import com.yoga.eucpsms.EucpsmsApplication;
import com.yoga.resource.ResourceApplication;
import com.yoga.UserApplication;
import com.yoga.utility.UtilityApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

@SpringBootApplication
@MapperScan("com.yoga.**.mapper")
public class EweddingApplication {

	private static Logger logger = LoggerFactory.getLogger(EweddingApplication.class);

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
		Object [] objects = new Object[]{
				CoreApplication.class,
				ResourceApplication.class,
				UserApplication.class,
				UtilityApplication.class,
				EucpsmsApplication.class,
				EweddingApplication.class};
		new SpringApplicationBuilder(objects)
				.web(true)
				.run(args);
	}
}
