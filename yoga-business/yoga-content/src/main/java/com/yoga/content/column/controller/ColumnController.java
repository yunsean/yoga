package com.yoga.content.column.controller;

import com.yoga.content.column.dto.*;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.content.column.vo.ColumnVo;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StringUtil;
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
import java.util.List;

@Controller("cmsColumnController")
@RequestMapping("/admin/cms/column")
@Api(tags = "CMS栏目管理")
public class ColumnController extends BaseController {

    @Autowired
    private ColumnService columnService;
    @Autowired
    private TemplateService templateService;

    @ApiIgnore
    @RequiresPermissions("cms_column")
    @RequestMapping("")
    public String allColumn(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Column> columns = columnService.tree(dto.getTid());
        model.put("columns", columns);
        model.put("param", dto.wrapAsMap());
        model.put("templates", templateService.list(dto.getTid(), null, true));
        return "/admin/cms/column/list";
    }

    @ResponseBody
    @ApiOperation("查询栏目列表")
    @GetMapping("/list.json")
    public ApiResults<ColumnVo> list(@ModelAttribute @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StringUtil.isNotBlank(dto.getParentCode())) {
            Column parent = columnService.get(dto.getTid(), dto.getParentCode());
            dto.setParentId(parent.getId());
        }
        List<Column> columns = columnService.list(dto.getTid(), dto.getParentId(), null, true, dto.getHidden());
        return new ApiResults<>(columns, ColumnVo.class);
    }
    @ResponseBody
    @ApiOperation("获取栏目详情")
    @GetMapping("/get.json")
    @RequiresAuthentication
    public ApiResult<ColumnVo> get(@ModelAttribute @Valid GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getId() == null && StringUtil.isBlank(dto.getCode())) throw new IllegalArgumentException("请输入栏目ID或者栏目编码！");
        Column column;
        if (StringUtil.isNotBlank(dto.getCode())) column = columnService.get(dto.getTid(), dto.getCode());
        else column = columnService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(column, ColumnVo.class);
    }
    @ResponseBody
    @ApiOperation(value = "增加新栏目")
    @PostMapping("/add.json")
    @RequiresPermissions("cms_column.add")
    public ApiResult add(@ModelAttribute @Valid AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        columnService.add(dto.getTid(), dto.getName(), dto.getCode(), dto.getRemark(), dto.getParentId(), dto.getTemplateId(), dto.isEnabled(), dto.isHidden());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation(value = "删除栏目")
    @DeleteMapping("/delete.json")
    @RequiresPermissions("cms_column.del")
    public ApiResult delete(@ModelAttribute @Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        columnService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("修改栏目信息")
    @RequiresPermissions("cms_column.update")
    @PostMapping("/update.json")
    public ApiResult update(@ModelAttribute @Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        columnService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getRemark(), dto.getParentId(), dto.getTemplateId(), dto.getEnabled(), dto.getHidden());
        return new ApiResult();
    }
}
