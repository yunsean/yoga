package com.yoga.content.layout.controller;

import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.content.enums.LayoutType;
import com.yoga.content.layout.dto.ListDto;
import com.yoga.content.layout.dto.ModifyDto;
import com.yoga.content.layout.dto.ShowDto;
import com.yoga.content.layout.model.Layout;
import com.yoga.content.layout.service.LayoutService;
import com.yoga.content.template.service.TemplateFieldService;
import com.yoga.core.controller.BaseWebController;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StrUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/cms/layout")
public class LayoutWebController extends BaseWebController {

    @Autowired
    private LayoutService layoutService;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private TemplateFieldService templateFieldService;
    @Autowired
    private ColumnService columnService;

    @RequiresAuthentication
    @RequestMapping("")
    public String list(ModelMap model, @Valid ListDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Layout> layouts = layoutService.find(dto.getTid(), dto.getTemplateId(), dto.getType());
        model.put("param", dto.wrapAsMap());
        model.put("layouts", layouts);
        model.put("layoutEnum", LayoutType.values());
        return "/layout/layouts";
    }

    @RequiresAuthentication
    @RequestMapping("/modify")
    public String update(ModelMap model, @Valid ModifyDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getId() != null) {
            Layout layout = layoutService.get(dto.getTid(), dto.getId());
            model.put("layout", layout);
            model.put("fields", layout.getFieldList());
        }
        model.put("param", dto.wrapAsMap());
        model.put("tenantId", dto.getTid());
        model.put("allFields", templateFieldService.allFields(dto.getTid(), dto.getTemplateId()));
        model.put("fineUploadurl", propertiesService.getFineUploadUrl());
        model.put("zuiUploadUrl", propertiesService.getZuiUploadUrl());
        model.put("baseUrl", propertiesService.getSysBaseurl());
        return "/layout/layout";
    }

    @RequestMapping("/show")
    public void show(HttpServletResponse response, @Valid ShowDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getColumnId() == null && StrUtil.isBlank(dto.getArticleId())) throw new BusinessException("请输入栏目ID或者文章ID");
        if (dto.getColumnId() != null && dto.getListLayoutId() == null) throw new BusinessException("请输入布局参数！");
        else if (StrUtil.isNotBlank(dto.getArticleId()) && dto.getDetailLayoutId() == null) throw new BusinessException("请输入布局参数！");
        Layout layout = layoutService.get(dto.getColumnId() == null ? dto.getDetailLayoutId() : dto.getListLayoutId());
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        String template = layout.getHtml();
        if (dto.getColumnId() != null) {
            String columnId = String.valueOf(dto.getColumnId());
            if (layout.getFields() != null) columnId += "&fields=" + layout.getFields().replace("*", ",");
            template = template.replace("#[[columnId]]", columnId);
            try {
                Column column = columnService.getColumn(dto.getTid(), dto.getColumnId());
                template = template.replaceAll("(?i)<title>.*</title>", "<title>" + column.getName() + "</title>");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (StrUtil.isNotBlank(dto.getArticleId())) {
            template = template.replace("#[[articleId]]", dto.getArticleId());
        }
        if (dto.getDetailLayoutId() != null) {
            template = template.replace("#[[detailLayoutId]]", String.valueOf(dto.getDetailLayoutId()));
        }
        template = template.replace("#[[tenantId]]", String.valueOf(dto.getTid()));
        writer.write(template);
        writer.flush();
        writer.close();
    }
}
