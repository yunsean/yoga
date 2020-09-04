package com.yoga.admin.freemarker;

import com.yoga.setting.customize.CustomPage;
import com.yoga.setting.service.SettingService;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class TeantTagsConfigurer {

    @Autowired
    private TenantDirective tenantDirective;
    @Autowired
    private SettingService settingService;
    @Autowired
    private FreeMarkerConfigurer configurer;

    @PostConstruct
    public void setupFreemarkShiro() throws TemplateModelException {
        configurer.getConfiguration().setSharedVariable("tenantTags", tenantDirective);
        CustomPage.setSettingService(settingService);
    }
}
