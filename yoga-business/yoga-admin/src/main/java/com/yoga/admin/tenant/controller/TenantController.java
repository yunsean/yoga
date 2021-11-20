package com.yoga.admin.tenant.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.tenant.dto.*;
import com.yoga.admin.tenant.vo.TenantMenuVo;
import com.yoga.admin.tenant.vo.TenantVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.base.BaseEnum;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.customize.CustomPage;
import com.yoga.setting.model.SettableItem;
import com.yoga.setting.model.Setting;
import com.yoga.setting.service.SettingService;
import com.yoga.tenant.menu.MenuItem;
import com.yoga.tenant.menu.MenuLoader;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.model.TenantCustomize;
import com.yoga.tenant.tenant.model.TenantMenu;
import com.yoga.tenant.tenant.model.TenantSetting;
import com.yoga.tenant.tenant.service.TemplateService;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.utility.alidns.service.AliDnsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.*;

@Controller
@Settable
@Api(tags = "租户管理")
@RequestMapping("/admin/tenant/tenant")
public class TenantController extends BaseController {


    @Value("${app.system.public-ip:}")
    private String publicIp;

    @Autowired
    private TenantService tenantService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private AliDnsService dnsService;

    @ApiIgnore
    @GetMapping("/list")
    @RequiresPermissions("gbl_tenant")
    public String allTenants(ModelMap model, CustomPage page, @Valid TenantListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Tenant> tenants = tenantService.list(dto.getName(), dto.getCode(), dto.getTemplateId(), null, page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("tenants", tenants.getList());
        model.put("page", new CommonPage(tenants));
        model.put("templates", templateService.list(null, 0, 1000).getList());
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/tenant/tenants";
    }
    @ApiIgnore
    @GetMapping("/modules")
    @RequiresPermissions("gbl_tenant")
    public String allModules(ModelMap model, @Valid TenantModuleDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<MenuItem> menuItems = MenuLoader.getInstance().getAllMenus(false);
        List<String> modules = null;
        String[] moduleArray = tenantService.getModules(dto.getTenantId());
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
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/tenant/modules";
    }
    @ApiIgnore
    @GetMapping("/menus")
    @RequiresPermissions("gbl_tenant")
    public String allMenus(ModelMap model, @Valid TenantMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<MenuItem> customMenus = tenantService.getMenus(dto.getTenantId());
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
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/tenant/menus";
    }
    @ApiIgnore
    @GetMapping("/settings")
    @RequiresPermissions("gbl_tenant")
    public String setting(ModelMap model, CustomPage page, @Valid TenantListSettingsDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String[] modules = tenantService.getModules(dto.getTenantId());
        String[] modeles1 = new String[modules.length + 1];
        System.arraycopy(modules, 0, modeles1, 0, modules.length);
        modeles1[modules.length] = "gbl_tenant";
        PageInfo<SettableItem> items = SettingService.allSettable(dto.getName(), modeles1, page.getPageIndex(), page.getPageSize(), true);
        List<Map<String, Object>> settings = new ArrayList<>();
        for (SettableItem item : items.getList()) {
            Setting setting = settingService.get(dto.getTenantId(), item.getModule(), item.getKey());
            if (setting != null) {
                item.setValue(setting.getValue());
                item.setShowValue(setting.getShowValue());
            } else {
                item.setValue(StringUtil.isNotBlank(item.getDefValue()) ? item.getDefValue() : null);
            }
            String url = item.getUrl();
            if (url == null) url = "";
            if (StringUtil.isNotBlank(url)) {
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
        model.put("param", dto.wrapAsMap());
        model.put("tenantId", dto.getTenantId());
        model.put("settings", settings);
        model.put("page", new CommonPage(items));
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/tenant/settings";
    }
    @ApiIgnore
    @GetMapping("/setting")
    @RequiresPermissions("gbl_tenant")
    @Settable(module = TenantService.ModuleName, key = TenantService.Key_Setting, name = "租户设置-全局设置", systemOnly = true)
    public String tenantSetting(ModelMap model, @Valid TenantSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantSetting setting = tenantService.readSetting(dto.getTenantId());
        model.put("setting", setting);
        model.put("tenantId", dto.getTenantId());
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/tenant/setting";
    }
    @ApiIgnore
    @GetMapping("/customize")
    @RequiresPermissions("gbl_tenant")
    @Settable(module = TenantService.ModuleName, key = TenantService.Key_Customize, name = "租户设置-框架自定义", systemOnly = true)
    public String tenantCustomize(ModelMap model, @Valid TenantSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantCustomize customize = tenantService.readCustomize(dto.getTenantId());
        model.put("customize", customize);
        model.put("tenantId", dto.getTenantId());
        model.put("tenantAlias", propertiesService.getTenantAlias());
        return "/admin/tenant/customize";
    }

    @ResponseBody
    @GetMapping("/list.json")
    @RequiresAuthentication
    @ApiOperation(value = "租户列表")
    public ApiResults<TenantVo> list(@ModelAttribute TenantListDto dto) {
        PageInfo<Tenant> tenants = tenantService.list(dto.getName(), dto.getCode(), dto.getTemplateId(), false, 0, 1000);
        return new ApiResults<>(tenants, TenantVo.class);
    }
    @ResponseBody
    @GetMapping("/get.json")
    @ApiOperation(value = "租户详情")
    public ApiResult<TenantVo> get(@ModelAttribute @Valid TenantGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Tenant tenant = tenantService.get(dto.getId());
        return new ApiResult<>(tenant, TenantVo.class);
    }
    @ResponseBody
    @GetMapping("/info.json")
    @ApiOperation(value = "当前租户详情")
    public ApiResult<TenantVo> info(@ModelAttribute @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Tenant tenant = tenantService.get(dto.getTid());
        return new ApiResult<>(tenant, TenantVo.class);
    }
    @ResponseBody
    @PostMapping("/add.json")
    @RequiresPermissions("gbl_tenant.add")
    @ApiOperation(value = "增加新租户")
    public ApiResult add(@Valid @ModelAttribute TenantAddDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        if (dto.getPassword().length() < 6) throw new IllegalArgumentException("管理员密码至少6个字符！");
        tenantService.add(dto.getName(), dto.getCode(), dto.getRemark(), dto.getTemplateId(), dto.getUsername(), dto.getPassword(), dto.getNickname(), dto.getMobile());
        if (StringUtil.isNotBlank(publicIp)) {
            try {
                dnsService.addARecord(dto.getCode(), publicIp, null);
            } catch (Exception ex) {
                return new ApiResult(1, "创建租户成功，但添加域名解析失败，请手动管理域名解析！");
            }
        }
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/update.json")
    @RequiresPermissions("gbl_tenant.update")
    @ApiOperation(value = "修改租户信息")
    public ApiResult update(@Valid @ModelAttribute TenantUpdateDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.update(dto.getId(), dto.getName(), dto.getRemark());
        return new ApiResult();
    }
    @ResponseBody
    @DeleteMapping("/delete.json")
    @RequiresPermissions("gbl_tenant.del")
    @ApiOperation(value = "删除现有租户")
    public ApiResult delete(@Valid @ModelAttribute TenantDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        Tenant tenant = tenantService.get(dto.getId());
        tenantService.delete(dto.getId());
        if (StringUtil.isNotBlank(publicIp) && StringUtil.isNotBlank(tenant.getCode())) {
            try {
                dnsService.deleteARecord(tenant.getCode(), null);
            } catch (Exception ex) {
                return new ApiResult(1, "删除租户成功，但删除域名解析失败，请手动管理域名解析！");
            }
        }
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/renew.json")
    @RequiresPermissions("gbl_tenant.del")
    @ApiOperation(value = "恢复删除的租户")
    public ApiResult renew(@Valid @ModelAttribute TenantRenewDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.renew(dto.getId(), dto.getCode());
        if (StringUtil.isNotBlank(publicIp)) {
            try {
                dnsService.addARecord(dto.getCode(), publicIp, null);
            } catch (Exception ex) {
                return new ApiResult(1, "恢复租户成功，但添加域名解析失败，请手动管理域名解析！");
            }
        }
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/repair.json")
    @RequiresPermissions("gbl_tenant.update")
    @ApiOperation(value = "修复租户权限")
    public ApiResult repair(@Valid @ModelAttribute TenantRepairDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        return new ApiResult<>(tenantService.repair(dto.getId()));
    }
    @ResponseBody
    @PostMapping("/module/set.json")
    @RequiresPermissions("gbl_tenant.update")
    @ApiOperation(value = "设置租户可用的模块")
    public ApiResult saveModules(@Valid @ModelAttribute TenantSetModuleDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        tenantService.setModules(dto.getTenantId(), dto.getModules());
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/menu/add.json")
    @RequiresPermissions("gbl_tenant.update")
    @ApiOperation(value = "增加租户新自定义菜单")
    public ApiResult addMenu(@Valid @ModelAttribute TenantAddMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.addMenu(dto.getTenantId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new ApiResult();
    }
    @ResponseBody
    @DeleteMapping("/menu/delete.json")
    @RequiresPermissions("gbl_tenant.update")
    @ApiOperation(value = "删除租户现有自定义菜单")
    public ApiResult deleteMenu(@Valid @ModelAttribute TenantDeleteMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.deleteMenu(dto.getTenantId(), dto.getMenuId());
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/menu/update.json")
    @RequiresPermissions("gbl_tenant.update")
    @ApiOperation(value = "修改租户自定义菜单")
    public ApiResult updateMenu(@Valid @ModelAttribute TenantUpdateMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.updateMenu(dto.getTenantId(), dto.getMenuId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new ApiResult();
    }
    @ApiOperation(value = "获取租户自定义菜单详情")
    @ResponseBody
    @GetMapping("/menu/get.json")
    @RequiresPermissions("gbl_tenant.update")
    public ApiResult<TenantMenuVo> getMenu(@Valid @ModelAttribute TenantGetMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantMenu menu = tenantService.getMenu(dto.getTenantId(), dto.getMenuId());
        return new ApiResult<>(menu, TenantMenuVo.class);
    }
    @ApiIgnore
    @ResponseBody
    @RequiresPermissions("gbl_tenant.update")
    @PostMapping("/setting/save.json")
    public ApiResult saveSetting(@Valid TenantSaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantSetting setting = new TenantSetting();
        setting.setPlatformName(dto.getPlatform());
        setting.setFooterRemark(dto.getFooter());
        setting.setResourcePrefix(dto.getResource());
        setting.setLoginBackUrl(dto.getLoginbg());
        setting.setLoginLogoUrl(dto.getLoginlogo());
        setting.setAdminIcon(dto.getAdminIcon());
        setting.setTopImageUrl(dto.getTopimage());
        setting.setMenuColor(dto.getMenuColor());
        tenantService.saveSetting(dto.getTenantId(), setting);
        return new ApiResult();
    }
    @ApiIgnore
    @ResponseBody
    @RequiresPermissions("gbl_tenant.update")
    @PostMapping("/customize/save.json")
    public ApiResult saveCustomize(@Valid TenantSaveCustomizeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantCustomize customize = new TenantCustomize();
        customize.setAdminIndex(dto.getAdminIndex());
        customize.setAdminLeft(dto.getAdminLeft());
        customize.setAdminLogin(dto.getAdminLogin());
        customize.setAdminTop(dto.getAdminTop());
        customize.setAdminWelcome(dto.getAdminWelcome());
        customize.setFrontIndex(dto.getFrontIndex());
        customize.setFrontLogin(dto.getFrontLogin());
        tenantService.saveCustomize(dto.getTenantId(), customize);
        return new ApiResult();
    }
    @ApiIgnore
    @RequiresPermissions("sys_config.update")
    @PostMapping("/settings/save.json")
    public ApiResult save(@RequestBody @Valid TenantSaveSettingsDto bean, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        settingService.save(bean.getTenantId(), bean.getSettings());
        return new ApiResult();
    }
}
