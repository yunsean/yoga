package com.yoga.admin.setting.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.setting.dto.SettingGetDto;
import com.yoga.admin.setting.dto.SettingListDto;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.base.BaseEnum;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.spring.SpringContext;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.annotation.SettableDataSource;
import com.yoga.setting.customize.CustomPage;
import com.yoga.setting.model.SaveItem;
import com.yoga.setting.model.SettableItem;
import com.yoga.setting.model.Setting;
import com.yoga.setting.service.SettingService;
import com.yoga.tenant.tenant.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "系统设置")
@Controller
@RequestMapping("/admin/system/setting")
@Settable(module = "sys_config", key = "common.page.size", name = "基础设置-每页条目数量", type = Long.class, defaultValue = "15")
public class SettingController extends BaseController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private SpringContext springContext;

    @ApiIgnore
    @RequestMapping("/list")
    @RequiresPermissions("sys_config")
    public String setting(ModelMap model, CustomPage page, @Valid SettingListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String[] modules = tenantService.getModules(dto.getTid());
        PageInfo<SettableItem> items = SettingService.allSettable(dto.getFilter(), modules, page.getPageIndex(), page.getPageSize(), false);
        List<Map<String, Object>> settings = new ArrayList<>();
        for (SettableItem item : items.getList()) {
            Setting setting = settingService.get(dto.getTid(), item.getModule(), item.getKey());
            if (setting != null) {
                item.setValue(setting.getValue());
                item.setShowValue(setting.getShowValue());
            } else {
                item.setValue(StringUtil.isNotBlank(item.getDefValue()) ? item.getDefValue() : null);
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
        model.put("param", dto.wrapAsMap());
        model.put("settings", settings);
        model.put("page", new CommonPage(items));
        return "/admin/system/settings";
    }

    @ApiIgnore
    @ResponseBody
    @RequiresPermissions("sys_config.update")
    @PostMapping("/save.json")
    public CommonResult save(@Valid BaseDto dto, @RequestBody @Valid List<SaveItem> bean, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        settingService.save(dto.getTid(), bean);
        return new CommonResult();
    }
    @ResponseBody
    @ApiOperation(value = "读取配置参数")
    @RequiresPermissions(value = {"sys_config", "sys_config.update"}, logical = Logical.OR)
    @GetMapping("/get.json")
    public CommonResult get(@Valid @ModelAttribute SettingGetDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Setting setting = settingService.get(dto.getTid(), dto.getModule(), dto.getKey());
        return new CommonResult(setting.getValue());
    }
}
