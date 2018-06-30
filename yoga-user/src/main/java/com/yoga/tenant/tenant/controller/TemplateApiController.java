package com.yoga.tenant.tenant.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.tenant.tenant.dto.*;
import com.yoga.tenant.tenant.model.TenantTemplate;
import com.yoga.tenant.tenant.service.TenantTemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Explain(value = "GLB租户模板管理", module = "gbl_tenant_template")
@RestController("tenantTemplateApiController")
@RequestMapping("/api/tenant/template")
public class TemplateApiController extends BaseApiController {

    @Autowired
    private TenantTemplateService tenantTemplateService;

    @Explain("模板列表")
    public CommonResult list() {
        PageList<TenantTemplate> tenants = tenantTemplateService.list(null, 0, 1000);
        return new CommonResult(new MapConverter<TenantTemplate>((TenantTemplate item, MapConverter.MapItem<String, Object> map) -> {
            map.set("id", item.getId())
                    .set("name", item.getName())
                    .set("remark", item.getRemark());
        }).build(tenants));
    }

    @Explain("增加模板")
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions("gbl_tenant_template.add")
    public CommonResult addTenant(HttpServletRequest request, @Valid AddTemplateDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantTemplateService.add(dto.getName(), dto.getRemark());
        return new CommonResult();
    }

    @Explain("修改模板")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("gbl_tenant_template.update")
    public CommonResult updateTenant(HttpServletRequest request, @Valid UpdateTemplateDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantTemplateService.update(dto.getId(), dto.getName(), dto.getRemark());
        return new CommonResult();
    }

    @Explain("删除模板")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("gbl_tenant_template.del")
    public CommonResult delTenant(HttpServletRequest request, @Valid DelTemplateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantTemplateService.delete(dto.getId());
        return new CommonResult();
    }

    @Explain("设置模板可用模块")
    @ResponseBody
    @RequestMapping("/module/set")
    @RequiresPermissions("gbl_tenant_template.update")
    public CommonResult saveModules(HttpServletRequest request, @Valid SetTemplateModelDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantTemplateService.saveModules(dto.getTemplateId(), dto.getModules());
        return new CommonResult();
    }

    @Explain("增加模板自定义菜单")
    @ResponseBody
    @RequestMapping("/menu/add")
    @RequiresPermissions("gbl_tenant_template.update")
    public CommonResult addMenu(HttpServletRequest request, @Valid AddTemplateMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantTemplateService.addMenu(dto.getTemplateId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new CommonResult();
    }

    @Explain("删除模板自定义菜单")
    @ResponseBody
    @RequestMapping("/menu/delete")
    @RequiresPermissions("gbl_tenant_template.update")
    public CommonResult delMenu(HttpServletRequest request, @Valid TemplateMenuDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantTemplateService.delMenu(dto.getMenuId());
        return new CommonResult();
    }

    @Explain("修改模板自定义菜单")
    @ResponseBody
    @RequestMapping("/menu/update")
    @RequiresPermissions("gbl_tenant_template.update")
    public CommonResult updateMenu(HttpServletRequest request, @Valid UpdateTemplateMenuDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        tenantTemplateService.updateMenu(dto.getMenuId(), dto.getCode(), dto.getGroup(), dto.getName(), dto.getUrl(), dto.getRemark(), dto.getSort());
        return new CommonResult();
    }
}
