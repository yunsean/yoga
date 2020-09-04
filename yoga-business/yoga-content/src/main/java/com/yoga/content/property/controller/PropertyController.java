package com.yoga.content.property.controller;

import com.yoga.content.property.dto.*;
import com.yoga.content.property.model.Property;
import com.yoga.content.property.service.PropertyService;
import com.yoga.content.property.vo.PropertyVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.base.BaseVo;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StringUtil;
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
import java.util.List;
import java.util.Optional;

@Api(tags = "CMS选项管理")
@Controller("cmsPropertyController")
@RequestMapping("/admin/cms/property")
public class PropertyController extends BaseController {

    @Autowired
    private PropertyService propertyService;

    @ApiIgnore
    @RequiresPermissions("cms_property")
    @RequestMapping("")
    public String property(ListDto dto, ModelMap model) {
        List<Property> properties = propertyService.list(dto.getTid(), 0L);
        List<Property> values = null;
        if (dto.getId() != null && dto.getId() != 0L) {
            values = propertyService.childrenOf(dto.getTid(), dto.getId(), true);
        } else if (StringUtil.isNotBlank(dto.getCode())) {
            Optional<Property> parent = properties.stream().filter(it-> dto.getCode().equals(it.getCode())).findAny();
            if (parent.isPresent()) {
                values = propertyService.childrenOf(dto.getTid(), parent.get().getId(), true);
                dto.setId(parent.get().getId());
            }
        }
        model.put("properies", properties);
        model.put("values", values);
        model.put("param", dto.wrapAsMap());
        return "/admin/cms/property/list";
    }

    @ResponseBody
    @ApiOperation("获取选项列表")
    @GetMapping("/list.json")
    public ApiResults<PropertyVo> allOptions(@Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Property> options = propertyService.list(dto.getTid(), null);
        return new ApiResults<>(options, PropertyVo.class);
    }
    @ResponseBody
    @ApiOperation("获取选项列表")
    @GetMapping("/tree.json")
    public ApiResults<PropertyVo> treeOptions(@Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Property> options;
        if (StringUtil.isBlank(dto.getCode())) {
            options = propertyService.childrenOf(dto.getTid(), 0, true);
        } else {
            Property parent = propertyService.get(dto.getTid(), dto.getCode());
            options = propertyService.childrenOf(dto.getTid(), parent.getId(), true);
        }
        return new ApiResults<>(expandProperty(options));
    }
    @ResponseBody
    @ApiOperation("获取选项信息")
    @GetMapping("/get.json")
    public ApiResult<PropertyVo> getOption(@Valid GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getId() == null && StringUtil.isBlank(dto.getCode())) throw new BusinessException("选项ID和选项编码不能为空！");
        Property property;
        if (dto.getId() != null) property = propertyService.get(dto.getTid(), dto.getId());
        else property = propertyService.get(dto.getTid(), dto.getCode());
        return new ApiResult<>(property, PropertyVo.class);
    }
    @ResponseBody
    @ApiOperation("获取选项值")
    @PostMapping("/value/get.json")
    @RequiresPermissions("cms_property.delete")
    public ApiResult<List<PropertyVo>> valuesOfOption(@Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Property parent = propertyService.get(dto.getTid(), dto.getCode());
        List<Property> optionens = propertyService.childrenOf(dto.getTid(), parent.getId(), false);
        return new ApiResult<>(expandProperty(optionens));
    }
    @ResponseBody
    @ApiOperation("增加新选项")
    @PostMapping("/add.json")
    @RequiresPermissions("cms_property.add")
    public ApiResult addOption(@Valid AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        propertyService.add(dto.getTid(), dto.getCode(), dto.getName(), dto.getParentId(), dto.getPoster());
        return new ApiResult();
    }
    @ResponseBody
    @DeleteMapping("/delete.json")
    @RequiresPermissions("cms_property.del")
    @ApiOperation(value = "删除现有选项")
    public ApiResult deleteOption(@Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        propertyService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("修改选项信息")
    @RequiresPermissions("cms_property.update")
    @RequestMapping("/update.json")
    public ApiResult modifyOption(@Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        propertyService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getParentId(), dto.getPoster());
        return new ApiResult();
    }
    private List<PropertyVo> expandProperty(List<Property> properties) {
        if (properties == null) return null;
        return BaseVo.copys(properties, PropertyVo.class, (po, vo)-> vo.setChildren(expandProperty(po.getChildren())));
    }
}
