package com.yoga.content;

import com.yoga.admin.AdminApplication;
import com.yoga.excelkit.ExcelkitApplication;
import com.yoga.logging.LoggingApplication;
import com.yoga.operator.OperatorApplication;
import com.yoga.resource.ResourceApplication;
import com.yoga.core.CoreApplication;
import com.yoga.setting.SettingApplication;
import com.yoga.tenant.TenantApplication;
import com.yoga.ueditor.UEditorApplication;
import com.yoga.utility.UtilityApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ContentApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(
				CoreApplication.class,
				LoggingApplication.class,
				ExcelkitApplication.class,
				OperatorApplication.class,
				ResourceApplication.class,
				SettingApplication.class,
				TenantApplication.class,
				UEditorApplication.class,
				UtilityApplication.class,

				AdminApplication.class,
				ContentApplication.class
		)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}
}
