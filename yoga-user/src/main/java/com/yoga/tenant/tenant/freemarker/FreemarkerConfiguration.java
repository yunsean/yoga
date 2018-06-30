package com.yoga.tenant.tenant.freemarker;

import com.yoga.user.basic.TenantPage;
import com.yoga.tenant.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FreemarkerConfiguration extends FreeMarkerAutoConfiguration.FreeMarkerWebConfiguration {

    @Autowired
    private TenantDirective tenantDirective;
    @Autowired
    private SettingService settingService;

    @Override
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = super.freeMarkerConfigurer();
        Map<String, Object> sharedVariables = new HashMap<String, Object>();
        sharedVariables.put("tenantTags", tenantDirective);
        configurer.setFreemarkerVariables(sharedVariables);
        TenantPage.setSettingService(settingService);
        return configurer;
    }
}
