package com.yoga.content.template.controller;

import com.yoga.content.template.dto.*;
import com.yoga.content.template.model.Template;
import com.yoga.content.template.model.TemplateField;
import com.yoga.content.template.service.TemplateFieldService;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.basic.TenantPage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Explain(value = "CMS模板管理", module = "cms_template")
@RestController
@RequestMapping("/api/cms/template")
public class TemplateApiController extends BaseApiController {

    @Autowired
    private TemplateService templateService = null;
    @Autowired
    private TemplateFieldService fieldService = null;

    @Explain("增加新模板")
    @RequiresPermissions("cms_template.add")
    @RequestMapping("/add")
    public CommonResult addTemplate(HttpServletRequest request, @Valid AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.addTemplate(dto.getTid(), dto.getName(), dto.getCode(), dto.getRemark(), dto.isEnabled());
        return new CommonResult();
    }

    @Explain("删除现有模板")
    @RequiresPermissions("cms_template.del")
    @RequestMapping("/delete")
    public CommonResult delTemplate(HttpServletRequest request, @Valid DelDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.delTemplate(dto.getTid(), dto.getId());
        return new CommonResult();
    }

    @RequiresAuthentication
    @Explain("模板列表")
    @RequestMapping("/list")
    public CommonResult allTemplates(HttpServletRequest request, TenantPage page, @Valid TenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<Template> templates = templateService.allTemplates(dto.getTid(), page.getPageIndex(), page.getPageSize());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<Template>() {
            @Override
            public void convert(Template item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("name", item.getName())
                        .set("code", item.getCode())
                        .set("remark", item.getRemark())
                        .set("enabled", item.isEnabled());
            }
        }).build(templates), templates.getPage());
    }

    @Explain("修改模板信息")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/update")
    public CommonResult modifyTemplate(HttpServletRequest request, @Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.updateTemplate(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getRemark(), dto.isEnabled());
        return new CommonResult();
    }

    @Explain("增加模板字段")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/field/add")
    public CommonResult addField(HttpServletRequest request, @Valid AddFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        fieldService.addField(dto.getTid(), dto.getTemplateId(), dto.getName(), dto.getCode(),
                dto.getHint(), dto.getType(), dto.getParam(), dto.getRemark(), dto.getPlaceholder(), dto.isEnabled());
        return new CommonResult();
    }

    @Explain("删除模板字段")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/field/delete")
    public CommonResult delField(HttpServletRequest request, @Valid DelFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        fieldService.delField(dto.getTid(), dto.getId());
        return new CommonResult();
    }

    @Explain("修改模板字段信息")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/field/update")
    public CommonResult modifyField(HttpServletRequest request, @Valid UpdateFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        fieldService.updateField(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getHint(),
                dto.getType(), dto.getParam(), dto.getRemark(), dto.getPlaceholder(), dto.isEnabled());
        return new CommonResult();
    }

    @RequiresAuthentication
    @Explain("模板字段列表")
    @RequestMapping("/field/list")
    public CommonResult allFields(HttpServletRequest request, @Valid ListFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Iterable<TemplateField> templates = fieldService.allFields(dto.getTid(), dto.getTemplateId());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<TemplateField>() {
            @Override
            public void convert(TemplateField item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("name", item.getName())
                        .set("code", item.getCode())
                        .set("type", item.getType().getCode())
                        .set("typeName", item.getType().getName())
                        .set("hint", item.getHint())
                        .set("enabled", item.isEnabled());
            }
        }).build(templates));
    }
}
