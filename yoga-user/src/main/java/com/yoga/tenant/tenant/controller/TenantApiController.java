package com.yoga.tenant.tenant.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.tenant.tenant.dto.*;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.model.TenantCustomize;
import com.yoga.tenant.tenant.model.TenantSetting;
import com.yoga.tenant.tenant.service.TenantService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Explain(value = "GLB租户管理", module = "gbl_tenant")
@RestController
@RequestMapping("/api/tenant")
public class TenantApiController extends BaseApiController {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private SettingService settingService;

    @Explain("租户列表")
    public CommonResult list() {
        PageList<Tenant> tenants = tenantService.tenants(null, null, 0, 1000);
        return new CommonResult(new MapConverter<Tenant>(new MapConverter.Converter<Tenant>() {
            @Override
            public void convert(Tenant item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("code", item.getCode())
                        .set("name", item.getName())
                        .set("remark", item.getRemark());
            }
        }).build(tenants));
    }

    @Explain("增加新租户")
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions("gbl_tenant.add")
    public CommonResult addTenant(HttpServletRequest request, @Valid AddTenantDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StrUtil.isBlank(dto.getUsername())) dto.setUsername("admin");
        if (StrUtil.isBlank(dto.getPassword())) dto.setPassword("123456");
        if (dto.getPassword().length() < 6) throw new IllegalArgumentException("管理员密码至少6个字符！");
        if (StrUtil.isBlank(dto.getFirstName()) && StrUtil.isBlank(dto.getLastName())) {
            dto.setFirstName("理员");
            dto.setLastName("管");
        }
        if (StrUtil.isNotBlank(dto.getPhone()) && !StrUtil.isMobileNo(dto.getPhone())) throw new IllegalArgumentException("管理员手机号码不合法");
        if (StrUtil.isBlank(dto.getPhone()) && StrUtil.isMobileNo(dto.getUsername())) dto.setPhone(dto.getUsername());
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.add(dto.getName(), dto.getCode(), dto.getRemark(), dto.getTemplateId(), dto.getUsername(), dto.getFirstName(), dto.getLastName(), dto.getPassword(), dto.getPhone());
        return new CommonResult();
    }

    @Explain("修改租户信息")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("gbl_tenant.update")
    public CommonResult updateTenant(HttpServletRequest request, @Valid UpdateTenantDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.update(dto);
        return new CommonResult();
    }

    @Explain("删除现有租户")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("gbl_tenant.del")
    public CommonResult delTenant(HttpServletRequest request, @Valid DelTenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.del(dto.getId());
        return new CommonResult();
    }

    @Explain("修复租户权限")
    @ResponseBody
    @RequestMapping("/repair")
    @RequiresPermissions("gbl_tenant.update")
    public CommonResult repairTenant(HttpServletRequest request, @Valid RepairTenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        TenantService.RepairResult result = tenantService.repair(dto.getId());
        return new CommonResult(result);
    }

    @Explain("设置租户可用的模块")
    @ResponseBody
    @RequestMapping("/module/set")
    @RequiresPermissions("gbl_tenant.update")
    public CommonResult saveModules(HttpServletRequest request, @Valid SetTenantModelDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.saveModules(dto.getTenantId(), dto.getModules());
        return new CommonResult();
    }

    @Explain("增加租户新自定义菜单")
    @ResponseBody
    @RequestMapping("/menu/add")
    @RequiresPermissions("gbl_tenant.update")
    public CommonResult addMenu(HttpServletRequest request, @Valid AddTenantMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.addMenu(dto.getTenantId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new CommonResult();
    }

    @Explain("删除租户现有自定义菜单")
    @ResponseBody
    @RequestMapping("/menu/delete")
    @RequiresPermissions("gbl_tenant.update")
    public CommonResult delMenu(HttpServletRequest request, @Valid TenantMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.delMenu(dto.getMenuId());
        return new CommonResult();
    }

    @Explain("修改租户自定义菜单")
    @ResponseBody
    @RequestMapping("/menu/update")
    @RequiresPermissions("gbl_tenant.update")
    public CommonResult updateMenu(HttpServletRequest request, @Valid UpdateTenantMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantService.updateMenu(dto.getMenuId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new CommonResult();
    }

    @Explain(exclude = true)
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/setting/save")
    public CommonResult saveSetting(HttpServletRequest request, @Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantSetting setting = new TenantSetting();
        setting.setPlatformName(dto.getPlatform());
        setting.setFooterRemark(dto.getFooter());
        setting.setResourcePrefix(dto.getResource());
        setting.setLoginBackUrl(dto.getLoginbg());
        setting.setLoginLogoUrl(dto.getLoginlogo());
        setting.setTopImageUrl(dto.getTopimage());
        setting.setRoleAlias(dto.getRole());
        setting.setDeptAlias(dto.getDept());
        setting.setDutyAlias(dto.getDuty());
        tenantService.saveSetting(dto.getTenantId(), setting);
        return new CommonResult();
    }

    @Explain(exclude = true)
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/customize/save")
    public CommonResult saveCustomize(HttpServletRequest request, @Valid SaveCustomizeDto dto, BindingResult bindingResult) {
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
        return new CommonResult();
    }

    @RequiresPermissions("gbl_settable.update")
    @RequestMapping("/settings/save")
    public CommonResult save(HttpServletRequest request, @RequestBody @Valid SaveSettingsDto bean, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        settingService.save(bean.getTenantId(), bean.getSettings());
        return new CommonResult();
    }
}
