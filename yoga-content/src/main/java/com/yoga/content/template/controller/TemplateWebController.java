package com.yoga.content.template.controller;

import com.yoga.content.property.service.PropertyService;
import com.yoga.content.template.dto.ListDto;
import com.yoga.content.template.dto.ListFieldDto;
import com.yoga.content.template.enums.FieldType;
import com.yoga.content.template.model.Template;
import com.yoga.content.template.model.TemplateField;
import com.yoga.content.template.service.TemplateFieldService;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.user.basic.TenantPage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cms")
public class TemplateWebController extends BaseWebController {

    @Autowired
    private TemplateService templateService = null;
    @Autowired
    private TemplateFieldService fieldService = null;
    @Autowired
    private PropertyService propertyService = null;
    @Autowired
    private PropertiesService propertiesService = null;

    @RequiresAuthentication
    @RequestMapping("/template")
    public String allTemplate(HttpServletRequest request, ModelMap model, TenantPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Page<Template> templates = templateService.findTemplate(dto.getTid(), dto.getFilter(), null, page.getPageIndex(), page.getPageSize());
        model.put("page", new CommonPage(templates));
        model.put("templates", new PageList<>(templates));
        Map<String, Object> params = new HashMap<String, Object>();
        model.put("param", params);
        return "/template/template";
    }

    @RequiresAuthentication
    @RequestMapping("/template/field")
    public String allTemplateList(HttpServletRequest request, ModelMap model, @Valid ListFieldDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<TemplateField> fields = fieldService.allFields(dto.getTid(), dto.getTemplateId());
        model.put("fields", fields);
        model.put("templateId", dto.getTemplateId());
        FieldType[] types = FieldType.values();
        model.put("fieldType", types);
        model.put("properties", propertyService.allProperties(dto.getTid()));
        model.put("uploadPath", propertiesService.getZuiUploadUrl());
        return "/template/field";
    }
}
