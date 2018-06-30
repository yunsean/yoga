package com.yoga.tenant.tenant.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.BaseEnum;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.model.SettableItem;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.tenant.tenant.dto.*;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.model.TenantCustomize;
import com.yoga.tenant.tenant.model.TenantSetting;
import com.yoga.tenant.tenant.model.TenantTemplate;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.tenant.tenant.service.TenantTemplateService;
import com.yoga.user.admin.menu.MenuItem;
import com.yoga.user.admin.menu.MenuLoader;
import com.yoga.user.basic.TenantPage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.*;

@Settable
@Controller
@RequestMapping("/tenant")
public class TenantWebController extends BaseWebController {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private TenantTemplateService tenantTemplateService;
    @Value("${yoga.tenant.expire-nerver:true}")
    private boolean nerverExpire = true;

    @RequiresAuthentication
    @RequestMapping("/templates")
    public String allTemplates(TenantPage page, @Valid QueryTemplateDto dto, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        PageList<TenantTemplate> templates = tenantTemplateService.list(dto.getName(), page.getPageIndex(), page.getPageSize());
        model.put("templates", templates);
        model.put("param", dto.wrapAsMap());
        model.put("page", templates.getPage());
        return "/tenant/template/templates";
    }

    @RequiresAuthentication
    @RequestMapping("/tenants")
    public String allTenants(TenantPage page, @Valid QueryTenantDto dto, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        PageList<Tenant> tenants = tenantService.tenants(dto.getName(), dto.getCode(), page.getPageIndex(), page.getPageSize());
        model.put("tenants", tenants);
        model.put("templates", tenantTemplateService.list(null, 0, 1000));
        model.put("page", tenants.getPage());
        model.put("param", dto.wrapAsMap());
        model.put("nerverExpire", nerverExpire);
        return "/tenant/tenants";
    }

    @RequiresAuthentication
    @RequestMapping("/modules")
    public String allModules(@Valid TenantModelDto dto, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        List<MenuItem> menuItems = MenuLoader.getInstance().getAllMenus(false);
        List<String> modules = null;
        String[] moduleArray = tenantService.modules(dto.getTenantId());
        if (moduleArray != null) modules = Arrays.asList(moduleArray);
        else modules = new ArrayList<>();
        for (MenuItem item : menuItems) {
            if (item.getChildren() == null) continue;
            for (MenuItem child : item.getChildren()) {
                if (modules.contains(child.getCode())) {
                    child.setChecked(true);
                } else {
                    child.setChecked(false);
                }
            }
        }
        model.put("tenantId", dto.getTenantId());
        model.put("modules", menuItems);
        return "/tenant/modules";
    }

    @RequiresAuthentication
    @RequestMapping("/menus")
    public String allMenus(ModelMap model, @Valid TenantModelDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        List<MenuItem> customMenus = tenantService.menus(dto.getTenantId());
        List<MenuItem> systemMenus = MenuLoader.getInstance().getAllMenus();
        Set<String> groups = new HashSet<>();
        if (customMenus != null) {
            for (MenuItem item : customMenus) {
                groups.add(item.getName());
                if (item.getChildren() != null) {
                    Collections.sort(item.getChildren(), new Comparator<MenuItem>() {
                        @Override
                        public int compare(MenuItem o1, MenuItem o2) {
                            return o1.getSort() - o2.getSort();
                        }
                    });
                }
            }
        }
        if (systemMenus != null) {
            for (MenuItem item : systemMenus) {
                groups.add(item.getName());
            }
        }
        model.put("tenantId", dto.getTenantId());
        model.put("menus", customMenus);
        model.put("groups", groups);
        return "/tenant/menus";
    }

    @RequiresAuthentication
    @RequestMapping("/settings")
    public String setting(ModelMap model, @Valid QuerySettingDto dto, TenantPage page, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String[] modules = tenantService.modules(dto.getTenantId());
        String[] modeles1 = new String[modules.length + 1];
        for (int i = 0; i < modules.length; i++) {
            modeles1[i] = modules[i];
        }
        modeles1[modules.length] = "gbl_tenant";
        PageList<SettableItem> items = settingService.allSettable(dto.getFilter(), modeles1, page.getPageIndex(), page.getPageSize(), true);
        List<Map<String, Object>> settings = new ArrayList<>();
        for (SettableItem item : items) {
            Setting setting = settingService.get(dto.getTenantId(), item.getModule(), item.getKey());
            if (setting != null) {
                item.setValue(setting.getValue());
                item.setShowValue(setting.getShowValue());
            } else {
                item.setValue(StrUtil.isNotBlank(item.getDefValue()) ? item.getDefValue() : null);
            }
            String url = item.getUrl();
            if (url == null) url = "";
            if (StrUtil.isNotBlank(url)) {
                if (url.indexOf('?') > 0) url += "&tenantId=" + dto.getTenantId();
                else url += "?tenantId=" + dto.getTenantId();
            }
            item.setUrl(url);
            Map<String, Object> map = new HashMap<>();
            if (item.getType() != null && BaseEnum.class.isAssignableFrom(item.getType())) {
                map.put("enums", item.getType().getEnumConstants());
            }
            map.put("setting", item);
            settings.add(map);
        }
        model.put("tenantId", dto.getTenantId());
        model.put("filter", dto.getFilter());
        model.put("settings", settings);
        model.put("page", items.getPage());
        return "/tenant/settings";
    }

    @RequiresAuthentication
    @Settable(key = TenantService.Setting_Key, name = "全局设置", module = TenantService.Setting_Module, systemOnly = true)
    @RequestMapping("/tenantSetting")
    public String tenantSetting(ModelMap model, @Valid TenantSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantSetting setting = tenantService.getSetting(dto.getTenantId());
        model.put("setting", setting);
        model.put("tenantId", dto.getTenantId());
        return "/tenant/setting";
    }


    @RequiresAuthentication
    @Settable(key = TenantService.Customize_Key, name = "框架自定义", module = TenantService.Setting_Module, systemOnly = true)
    @RequestMapping("/tenantCustomize")
    public String tenantCustomize(ModelMap model, @Valid TenantSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantCustomize customize = tenantService.getCustomize(dto.getTenantId());
        model.put("customize", customize);
        model.put("tenantId", dto.getTenantId());
        return "/tenant/customize";
    }

    @RequiresAuthentication
    @RequestMapping("/template/modules")
    public String templateModules(@Valid TemplateModelDto dto, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        List<MenuItem> menuItems = MenuLoader.getInstance().getAllMenus(false);
        List<String> modules;
        String[] moduleArray = tenantTemplateService.modules(dto.getTemplateId());
        if (moduleArray != null) modules = Arrays.asList(moduleArray);
        else modules = new ArrayList<>();
        for (MenuItem item : menuItems) {
            if (item.getChildren() == null) continue;
            for (MenuItem child : item.getChildren()) {
                if (modules.contains(child.getCode())) {
                    child.setChecked(true);
                } else {
                    child.setChecked(false);
                }
            }
        }
        model.put("templateId", dto.getTemplateId());
        model.put("modules", menuItems);
        return "/tenant/template/modules";
    }

    @RequiresAuthentication
    @RequestMapping("/template/menus")
    public String templateMenus(ModelMap model, @Valid TemplateModelDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        List<MenuItem> customMenus = tenantTemplateService.menus(dto.getTemplateId());
        List<MenuItem> systemMenus = MenuLoader.getInstance().getAllMenus();
        Set<String> groups = new HashSet<>();
        if (customMenus != null) {
            for (MenuItem item : customMenus) {
                groups.add(item.getName());
                if (item.getChildren() != null) {
                    Collections.sort(item.getChildren(), new Comparator<MenuItem>() {
                        @Override
                        public int compare(MenuItem o1, MenuItem o2) {
                            return o1.getSort() - o2.getSort();
                        }
                    });
                }
            }
        }
        if (systemMenus != null) {
            for (MenuItem item : systemMenus) {
                groups.add(item.getName());
            }
        }
        model.put("templateId", dto.getTemplateId());
        model.put("menus", customMenus);
        model.put("groups", groups);
        return "/tenant/template/menus";
    }

    @RequiresAuthentication
    @RequestMapping("/template/settings")
    public String templateSetting(ModelMap model, @Valid QuerySettingDto dto, TenantPage page, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String[] modules = tenantTemplateService.modules(dto.getTenantId());
        String[] modeles1 = new String[modules.length + 1];
        for (int i = 0; i < modules.length; i++) {
            modeles1[i] = modules[i];
        }
        modeles1[modules.length] = "gbl_tenant";
        PageList<SettableItem> items = settingService.allSettable(dto.getFilter(), modeles1, page.getPageIndex(), page.getPageSize(), true);
        List<Map<String, Object>> settings = new ArrayList<>();
        for (SettableItem item : items) {
            Setting setting = settingService.get(dto.getTenantId(), item.getModule(), item.getKey());
            if (setting != null) {
                item.setValue(setting.getValue());
                item.setShowValue(setting.getShowValue());
            } else {
                item.setValue(StrUtil.isNotBlank(item.getDefValue()) ? item.getDefValue() : null);
            }
            String url = item.getUrl();
            if (url == null) url = "";
            if (StrUtil.isNotBlank(url)) {
                if (url.indexOf('?') > 0) url += "&tenantId=" + dto.getTenantId();
                else url += "?tenantId=" + dto.getTenantId();
            }
            item.setUrl(url);
            Map<String, Object> map = new HashMap<>();
            if (item.getType() != null && BaseEnum.class.isAssignableFrom(item.getType())) {
                map.put("enums", item.getType().getEnumConstants());
            }
            map.put("setting", item);
            settings.add(map);
        }
        model.put("tenantId", dto.getTenantId());
        model.put("filter", dto.getFilter());
        model.put("settings", settings);
        model.put("page", items.getPage());
        return "/tenant/template/settings";
    }
}
