package com.yoga.admin.template.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.template.dto.*;
import com.yoga.admin.template.vo.TemplateMenuVo;
import com.yoga.admin.template.vo.TemplateVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseEnum;
import com.yoga.core.data.*;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.customize.CustomPage;
import com.yoga.setting.model.SettableItem;
import com.yoga.setting.model.Setting;
import com.yoga.setting.service.SettingService;
import com.yoga.tenant.menu.MenuItem;
import com.yoga.tenant.menu.MenuLoader;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.model.TenantMenu;
import com.yoga.tenant.tenant.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.*;

@Api(tags = "租户模板管理")
@Controller("tenantTemplateApiController")
@RequestMapping("/admin/tenant/template")
public class TemplateController extends BaseController {

    @Autowired
    private TemplateService templateService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private PropertiesService propertiesService;

    @ApiIgnore
    @RequiresPermissions("gbl_tenant_template")
    @RequestMapping("/list")
    public String list(ModelMap model, CustomPage page, @Valid TemplateListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Tenant> templates = templateService.list(dto.getName(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("templates", templates.getList());
        model.put("page", new CommonPage(templates));
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/template/templates";
    }
    @ApiIgnore
    @RequiresPermissions("gbl_tenant_template")
    @RequestMapping("/modules")
    public String templateModules(@Valid TemplateModuleDto dto, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<MenuItem> menuItems = MenuLoader.getInstance().getAllMenus(false);
        List<String> modules;
        String[] moduleArray = templateService.getModules(dto.getTemplateId());
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
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/template/modules";
    }
    @ApiIgnore
    @RequiresPermissions("gbl_tenant_template")
    @RequestMapping("/menus")
    public String templateMenus(ModelMap model, @Valid TemplateMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<MenuItem> customMenus = templateService.getMenus(dto.getTemplateId());
        List<MenuItem> systemMenus = MenuLoader.getInstance().getAllMenus();
        Set<String> groups = new HashSet<>();
        if (customMenus != null) {
            for (MenuItem item : customMenus) {
                groups.add(item.getName());
                if (item.getChildren() != null) {
                    item.getChildren().sort(Comparator.comparing(MenuItem::getSort));
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
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/template/menus";
    }
    @ApiIgnore
    @RequiresPermissions("gbl_tenant_template")
    @RequestMapping("/settings")
    public String templateSetting(ModelMap model, CustomPage page, @Valid TemplateListSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String[] modules = templateService.getModules(dto.getTemplateId());
        String[] modeles1 = new String[modules.length + 1];
        System.arraycopy(modules, 0, modeles1, 0, modules.length);
        modeles1[modules.length] = "gbl_tenant";
        PageInfo<SettableItem> items = SettingService.allSettable(dto.getName(), modeles1, page.getPageIndex(), page.getPageSize(), true);
        List<Map<String, Object>> settings = new ArrayList<>();
        for (SettableItem item : items.getList()) {
            Setting setting = settingService.get(dto.getTemplateId(), item.getModule(), item.getKey());
            if (setting != null) {
                item.setValue(setting.getValue());
                item.setShowValue(setting.getShowValue());
            } else {
                item.setValue(StringUtil.isNotBlank(item.getDefValue()) ? item.getDefValue() : null);
            }
            String url = item.getUrl();
            if (url == null) url = "";
            if (StringUtil.isNotBlank(url)) {
                if (url.indexOf('?') > 0) url += "&tenantId=" + dto.getTemplateId();
                else url += "?tenantId=" + dto.getTemplateId();
            }
            item.setUrl(url);
            Map<String, Object> map = new HashMap<>();
            if (item.getType() != null && BaseEnum.class.isAssignableFrom(item.getType())) {
                map.put("enums", item.getType().getEnumConstants());
            }
            map.put("setting", item);
            settings.add(map);
        }
        model.put("param", dto.wrapAsMap());
        model.put("templateId", dto.getTemplateId());
        model.put("settings", settings);
        model.put("page", new CommonPage(items));
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/template/settings";
    }

    @ResponseBody
    @GetMapping("/list.json")
    @ApiOperation(value = "模板列表")
    public ApiResults<TemplateVo> list(@ModelAttribute @Valid TemplateListDto dto, BindingResult bindingResult) {
        PageInfo<Tenant> templates = templateService.list(dto.getName(), 0, 1000);
        return new ApiResults<>(templates, TemplateVo.class);
    }
    @ResponseBody
    @GetMapping("/get.json")
    @ApiOperation(value = "模板详情")
    public ApiResult<TemplateVo> get(@ModelAttribute @Valid TemplateGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Tenant template = templateService.get(dto.getId());
        return new ApiResult<>(template, TemplateVo.class);
    }
    @ApiOperation(value = "增加模板")
    @ResponseBody
    @PostMapping("/add.json")
    @RequiresPermissions("gbl_tenant_template.add")
    public ApiResult add(@ModelAttribute @Valid TemplateAddDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.add(dto.getName(), dto.getRemark());
        return new ApiResult();
    }
    @ApiOperation(value = "修改模板")
    @ResponseBody
    @PostMapping("/update.json")
    @RequiresPermissions("gbl_tenant_template.update")
    public ApiResult update(@ModelAttribute @Valid TemplateUpdateDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.update(dto.getId(), dto.getName(), dto.getRemark());
        return new ApiResult();
    }
    @ApiOperation(value = "删除模板")
    @ResponseBody
    @DeleteMapping("/delete.json")
    @RequiresPermissions("gbl_tenant_template.del")
    public ApiResult delete(@ModelAttribute @Valid TemplateDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.delete(dto.getId());
        return new ApiResult();
    }

    @ApiOperation(value = "设置模板可用模块")
    @ResponseBody
    @PostMapping("/module/set.json")
    @RequiresPermissions("gbl_tenant_template.update")
    public ApiResult saveModules(@Valid @ModelAttribute TemplateSetModuleDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.setModules(dto.getTemplateId(), dto.getModules());
        return new ApiResult();
    }

    @ApiOperation(value = "增加模板自定义菜单")
    @ResponseBody
    @PostMapping("/menu/add.json")
    @RequiresPermissions("gbl_tenant_template.update")
    public ApiResult addMenu(@Valid @ModelAttribute TemplateAddMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.addMenu(dto.getTemplateId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new ApiResult();
    }
    @ApiOperation(value = "删除模板自定义菜单")
    @ResponseBody
    @DeleteMapping("/menu/delete.json")
    @RequiresPermissions("gbl_tenant_template.update")
    public ApiResult deleteMenu(@Valid @ModelAttribute TemplateDeleteMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.deleteMenu(dto.getTemplateId(), dto.getMenuId());
        return new ApiResult();
    }
    @ApiOperation(value = "获取模板自定义菜单详情")
    @ResponseBody
    @GetMapping("/menu/get.json")
    @RequiresPermissions("gbl_tenant_template.update")
    public ApiResult<TemplateMenuVo> getMenu(@Valid @ModelAttribute TemplateGetMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantMenu menu = templateService.getMenu(dto.getTemplateId(), dto.getMenuId());
        return new ApiResult<>(menu, TemplateMenuVo.class);
    }
    @ApiOperation(value = "修改模板自定义菜单")
    @ResponseBody
    @PostMapping("/menu/update.json")
    @RequiresPermissions("gbl_tenant_template.update")
    public ApiResult updateMenu(@Valid @ModelAttribute TemplateUpdateMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.updateMenu(dto.getTemplateId(), dto.getMenuId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new ApiResult();
    }
}
