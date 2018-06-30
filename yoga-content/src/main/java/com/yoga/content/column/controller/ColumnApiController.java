package com.yoga.content.column.controller;

import com.yoga.content.column.dto.AddDto;
import com.yoga.content.column.dto.DelDto;
import com.yoga.content.column.dto.ListDto;
import com.yoga.content.column.dto.UpdateDto;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

@Explain(value = "CMS栏目管理", module = "cms_column")
@RestController
@RequestMapping("/api/cms/column")
public class ColumnApiController extends BaseApiController {

    @Autowired
    private ColumnService columnService = null;

    @Explain("增加新栏目")
    @RequiresPermissions("cms_column.add")
    @RequestMapping("/add")
    public CommonResult addColumn(HttpServletRequest request, @Valid AddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        columnService.addCoumn(dto.getTid(), dto.getName(), dto.getCode(), dto.getParentId(), dto.getTemplateId(), dto.isEnabled());
        return new CommonResult();
    }

    @Explain("删除现有栏目")
    @RequiresPermissions("cms_column.del")
    @RequestMapping("/delete")
    public CommonResult delColumn(HttpServletRequest request, @Valid DelDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        columnService.delColumn(dto.getTid(), dto.getId());
        return new CommonResult();
    }

    @Explain("查询栏目列表")
    @RequestMapping("/list")
    public CommonResult columnsOfId(HttpServletRequest request, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (null == dto.getEnabled()) dto.setEnabled(false);
        Iterable<Column> columns = null;
        if (dto.getParentId() != null && dto.getParentId() != 0) columns = columnService.childrenOf(dto.getTid(), dto.getParentId());
        else if (StrUtil.isNotBlank(dto.getParentCode())) columns = columnService.childrenOf(dto.getTid(), dto.getParentCode());
        else if (StrUtil.isNotBlank(dto.getCode())) columns = columnService.childrenOf(dto.getTid(), dto.getCode(), dto.getEnabled());
        else columns = columnService.allColumns(dto.getTid());
        return new CommonResult(expandColumn(columns));
    }

    @Explain("修改栏目信息")
    @RequiresPermissions("cms_column.update")
    @RequestMapping("/update")
    public CommonResult modifyColumn(HttpServletRequest request, @Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        columnService.updateColumn(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getTemplateId(), dto.isEnabled(), dto.getParentId());
        return new CommonResult();
    }

    private ArrayList<Map<String, Object>> expandColumn(Iterable<Column> columns) {
        if (columns == null) return null;
        return new MapConverter<>(new MapConverter.Converter<Column>() {
            @Override
            public void convert(Column item, MapConverter.MapItem<String, Object> map) {
                ArrayList<Map<String, Object>> children = expandColumn(item.getChildren());
                if (children == null || children.size() < 1) children = null;
                map.set("id", item.getId())
                        .set("name", item.getName())
                        .set("code", item.getCode())
                        .set("enabled", item.isEnabled())
                        .set("templateId", item.getTemplateId())
                        .set("children", children);
            }
        }).build(columns);
    }
}
