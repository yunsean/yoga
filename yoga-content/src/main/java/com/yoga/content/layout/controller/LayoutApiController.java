package com.yoga.content.layout.controller;

import com.yoga.content.layout.dto.*;
import com.yoga.content.layout.model.Layout;
import com.yoga.content.layout.service.LayoutService;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Explain(value = "CMS模板布局", module = "cms_template", exclude = true)
@RequestMapping("/api/cms/layout")
@RestController
public class LayoutApiController extends BaseApiController {

    @Autowired
    private LayoutService layoutService;

    @Explain(exclude = true)
    @RequestMapping("/list")
    public CommonResult list(@Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Layout> layout = layoutService.find(dto.getTid(), dto.getTemplateId(), dto.getType());
        return new CommonResult(layout);
    }

    @Explain("查询布局")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/get")
    public CommonResult getLayout(@Valid GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Layout layout = layoutService.get(dto.getTid(), dto.getId());
        return new CommonResult(layout);
    }

    @Explain("修改布局")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/update")
    public CommonResult updateLayout(@Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        layoutService.update(dto.getTid(), dto.getId(), dto.getTitle(), dto.getImage(), dto.getFields(), dto.getHtml(), dto.getCss(), dto.getJs(), dto.getAccessories());
        return new CommonResult();
    }

    @Explain("删除布局")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/delete")
    public CommonResult delLayout(@Valid DelDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        layoutService.delete(dto.getTid(), dto.getId());
        return new CommonResult();
    }

    @Explain("新增布局")
    @RequiresPermissions("cms_template.update")
    @RequestMapping("/add")
    public CommonResult addLayout(@Valid AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        layoutService.add(dto.getTid(), dto.getTemplateId(), dto.getType(), dto.getTitle(), dto.getImage(), dto.getFields(), dto.getHtml(), dto.getCss(), dto.getJs(), dto.getAccessories());
        return new CommonResult();
    }

}
