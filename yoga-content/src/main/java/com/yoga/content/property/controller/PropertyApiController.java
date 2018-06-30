package com.yoga.content.property.controller;

import com.yoga.content.property.dto.*;
import com.yoga.content.property.model.Property;
import com.yoga.content.property.service.PropertyService;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.user.basic.TenantDto;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Explain(value = "CMS选项管理", module = "cms_property")
@RestController
@RequestMapping("/api/cms/property")
public class PropertyApiController extends BaseApiController {

    @Autowired
    private PropertyService propertyService = null;

//    @RequiresAuthentication
    @Explain("选项列表")
    @RequestMapping("/list")
    public CommonResult allOptions(HttpServletRequest request, @Valid TenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Iterable<Property> options = propertyService.allProperties(dto.getTid());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<Property>() {
            @Override
            public void convert(Property item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("code", item.getCode())
                        .set("name", item.getName());
            }
        }).build(options));
    }

    @Explain("增加新选项")
    @RequiresPermissions("cms_property.add")
    @RequestMapping("/add")
    public CommonResult addOption(HttpServletRequest request, @Valid AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        propertyService.addProperty(dto.getTid(), dto.getCode(), dto.getName());
        return new CommonResult();
    }

    @Explain("删除现有选项")
    @RequiresPermissions("cms_property.del")
    @RequestMapping("/delete")
    public CommonResult deleteOption(HttpServletRequest request, @Valid DelDto dto, BindingResult bindingResult) {
        propertyService.delProperty(dto.getTid(), dto.getId());
        return new CommonResult();
    }

//    @RequiresAuthentication
    @Explain("列出选项值")
    @RequestMapping("/value/list")
    public CommonResult valuesOfOption(HttpServletRequest request, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Property> optionens = propertyService.allValues(dto.getTid(), dto.getCode());
        return new CommonResult(expandProperty(optionens));
    }

    @Explain("增加选项值")
    @RequiresPermissions("cms_property.add")
    @RequestMapping("/value/add")
    public CommonResult addOptionValue(HttpServletRequest request, @Valid AddValueDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        propertyService.addValue(dto.getTid(), dto.getParentId(), dto.getName(), dto.getCode());
        return new CommonResult();
    }

    @Explain("删除选项值")
    @RequiresPermissions("cms_property.del")
    @RequestMapping("/value/delete")
    public CommonResult deleteValue(HttpServletRequest request, @Valid DelValueDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        propertyService.delProperty(dto.getTid(), dto.getId());
        return new CommonResult();
    }


    @Explain("修改选项信息")
    @RequiresPermissions("cms_property.update")
    @RequestMapping("/update")
    public CommonResult modifyOption(HttpServletRequest request, @Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        propertyService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getParentId());
        return new CommonResult();
    }

    @RequiresAuthentication
    @Explain("列出选项树（包含选项和值）")
    @RequestMapping("/all/list")
    public CommonResult allOptionAndValue(HttpServletRequest request, @Valid TenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Iterable<Property> options = propertyService.allProperties(dto.getTid());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<Property>() {
            @Override
            public void convert(Property item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("code", item.getCode())
                        .set("name", item.getName())
                        .set("values", expandProperty(item.getChildren()));
            }
        }).build(options));
    }

    private ArrayList<Map<String, Object>> expandProperty(Iterable<Property> optionens) {
        if (optionens == null) return null;
        return new MapConverter<>(new MapConverter.Converter<Property>() {
            @Override
            public void convert(Property item, MapConverter.MapItem<String, Object> map) {
                ArrayList<Map<String, Object>> children = expandProperty(item.getChildren());
                if (children == null || children.size() < 1) children = null;
                map
                        .set("id", item.getId())
                        .set("name", item.getName())
                        .set("value", item.getValue())
                        .set("children", children);
            }
        }).build(optionens);
    }
}
