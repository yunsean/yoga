package com.yoga.tenant.setting.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.BaseEnum;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.spring.SpringContext;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.SettableDataSource;
import com.yoga.tenant.setting.dto.QuerySettingDto;
import com.yoga.tenant.setting.model.SettableItem;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.user.basic.TenantPage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/setting")
@Settable(module = "gbl_settable", key = "common.page.size", name = "基础设置-每页条目数量", type = Long.class, defaultValue = "15")
public class SettingWebController extends BaseWebController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private SpringContext springContext;

    @RequiresAuthentication
    @RequestMapping("/list")
    public String setting(HttpServletRequest request, ModelMap model, @Valid QuerySettingDto dto, TenantPage page, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String[] modules = tenantService.modules(dto.getTid());
        PageList<SettableItem> items = settingService.allSettable(dto.getFilter(), modules, page.getPageIndex(), page.getPageSize(), false);
        List<Map<String, Object>> settings = new ArrayList<>();
        for (SettableItem item : items) {
            Setting setting = settingService.get(dto.getTid(), item.getModule(), item.getKey());
            if (setting != null) {
                item.setValue(setting.getValue());
                item.setShowValue(setting.getShowValue());
            } else {
                item.setValue(StrUtil.isNotBlank(item.getDefValue()) ? item.getDefValue() : null);
            }
            Map<String, Object> map = new HashMap<>();
            if (item.getType() != null && BaseEnum.class.isAssignableFrom(item.getType())) {
                map.put("enums", item.getType().getEnumConstants());
            } else if (item.getType() != null && SettableDataSource.class.isAssignableFrom(item.getType())) {
                try {
                    SettableDataSource dataSource = (SettableDataSource) springContext.getApplicationContext().getBean(item.getType());
                    List<SettableDataSource.SettableDataItem> values = dataSource.allItems(dto.getTid(), item.getModule(), item.getKey());
                    map.put("values", values);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            map.put("setting", item);
            settings.add(map);
        }
        model.put("filter", dto.getFilter());
        model.put("settings", settings);
        model.put("page", items.getPage());
        return "/setting/settings";
    }
}
