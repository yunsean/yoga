package com.yoga.content.column.controller;

import com.yoga.content.column.dto.FilterDto;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.controller.BaseWebController;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StrUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ColumnWebController extends BaseWebController {

    @Autowired
    private TemplateService templateService = null;
    @Autowired
    private ColumnService columnService = null;

    @RequiresAuthentication
    @RequestMapping("/column")
    public String allColumn(HttpServletRequest request, ModelMap model, @Valid FilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("filter", dto.getFilter());
        List<Column> columns = columnService.columnTreeOf(dto.getTid(), dto.getFilter());
        model.put("columns", columns);
        model.put("query", StrUtil.map2Query(params));
        model.put("param", params);
        model.put("templates", templateService.allTemplates(dto.getTid()));
        return "/column/column";
    }
}
