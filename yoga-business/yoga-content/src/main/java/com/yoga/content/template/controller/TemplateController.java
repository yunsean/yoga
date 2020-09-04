package com.yoga.content.template.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.content.property.service.PropertyService;
import com.yoga.content.template.dto.*;
import com.yoga.content.template.enums.FieldType;
import com.yoga.content.template.model.Template;
import com.yoga.content.template.model.TemplateField;
import com.yoga.content.template.service.TemplateService;
import com.yoga.content.template.vo.TemplateFieldVo;
import com.yoga.content.template.vo.TemplateVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.ApiResult;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller("cmsTemplateController")
@RequestMapping("/admin/cms/template")
@Api(tags = "CMS模板管理")
public class TemplateController extends BaseController {

    @Autowired
    private TemplateService templateService;
    @Autowired
    private PropertyService propertyService;

    @ApiIgnore
    @RequiresPermissions("cms_template")
    @RequestMapping("")
    public String allTemplate(ModelMap model, CommonPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Template> templates = templateService.list(dto.getTid(), dto.getFilter(), null, page.getPageIndex(), page.getPageSize());
        model.put("templates", templates.getList());
        model.put("page", new CommonPage(templates));
        model.put("param", dto.wrapAsMap());
        return "/admin/cms/template/list";
    }
    @ApiIgnore
    @RequiresPermissions("cms_template")
    @RequestMapping("/field")
    public String allTemplateList(ModelMap model, @Valid ListFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<TemplateField> fields = templateService.listField(dto.getTemplateId(), dto.getName(), null, null);
        model.put("fields", fields);
        model.put("fieldTypes", FieldType.values());
        model.put("properties", propertyService.list(dto.getTid(), 0L));
        model.put("param", dto.wrapAsMap());
        return "/admin/cms/template/field";
    }

    @ResponseBody
    @ApiOperation(value = "添加新模板")
    @PostMapping("/add.json")
    @RequiresPermissions("cms_template.add")
    public ApiResult addTemplate(@Valid AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.add(dto.getTid(), dto.getName(), dto.getCode(), dto.getRemark(), dto.isEnabled());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("删除现有模板")
    @DeleteMapping("/delete.json")
    @RequiresPermissions("cms_template.del")
    public ApiResult delTemplate(@Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("模板列表")
    @RequiresAuthentication
    @GetMapping("/list.json")
    public ApiResults<TemplateVo> allTemplates(CommonPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Template> templates = templateService.list(dto.getTid(), dto.getFilter(), true, page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(templates, TemplateVo.class);
    }
    @ResponseBody
    @ApiOperation("获取模板信息")
    @RequiresAuthentication
    @GetMapping("/get.json")
    public ApiResult<TemplateVo> allTemplates(CommonPage page, @Valid GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Template template = templateService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(template, TemplateVo.class);
    }
    @ResponseBody
    @ApiOperation("修改模板信息")
    @RequiresPermissions("cms_template.update")
    @PostMapping("/update.json")
    public ApiResult modifyTemplate(@Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getRemark(), dto.isEnabled());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("启动模板")
    @RequiresPermissions("cms_template.update")
    @PostMapping("/enable.json")
    public ApiResult enableTemplate(@Valid EnableDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.update(dto.getTid(), dto.getId(), null, null, null, !dto.isDisabled());
        return new ApiResult();
    }
    @ResponseBody
    @RequiresAuthentication
    @ApiOperation("模板字段列表")
    @GetMapping("/field/list.json")
    public ApiResults<TemplateFieldVo> allFields(@Valid ListFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<TemplateField> fields = templateService.listField(dto.getTemplateId(), dto.getName(), null, true);
        return new ApiResults<>(fields, TemplateFieldVo.class);
    }
    @ResponseBody
    @RequiresAuthentication
    @ApiOperation("获取字段信息")
    @GetMapping("/field/get.json")
    public ApiResult<TemplateFieldVo> getField(@Valid GetFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TemplateField field = templateService.getField(dto.getTid(), dto.getId());
        return new ApiResult<>(field, TemplateFieldVo.class);
    }
    @ResponseBody
    @ApiOperation("增加模板字段")
    @RequiresPermissions("cms_template.update")
    @PostMapping("/field/add.json")
    public ApiResult addField(@Valid AddFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.addField(dto.getTid(), dto.getTemplateId(), dto.getName(), dto.getCode(),
                dto.getHint(), dto.getType(), dto.getParam(), dto.getRemark(), dto.getPlaceholder(), dto.isEnabled(), 0);
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("删除模板字段")
    @RequiresPermissions("cms_template.update")
    @DeleteMapping("/field/delete.json")
    public ApiResult delField(@Valid DeleteFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.deleteField(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("修改模板字段信息")
    @RequiresPermissions("cms_template.update")
    @PostMapping("/field/update.json")
    public ApiResult modifyField(@Valid UpdateFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.updateField(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getHint(), dto.getType(), dto.getParam(), dto.getRemark(), dto.getPlaceholder(), dto.getEnabled(), null);
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("启用模板字段")
    @RequiresPermissions("cms_template.update")
    @PostMapping("/field/enable.json")
    public ApiResult enableField(@Valid EnableFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.updateField(dto.getTid(), dto.getId(), null, null, null, null, null, null, null, !dto.isDisabled(), null);
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("设置模板字段排序")
    @RequiresPermissions("cms_template.update")
    @PostMapping("/field/sort.json")
    public ApiResult sortField(@Valid SortFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        templateService.updateField(dto.getTid(), dto.getId(), null, null, null, null, null, null, null, null, dto.getSort());
        return new ApiResult();
    }
}
