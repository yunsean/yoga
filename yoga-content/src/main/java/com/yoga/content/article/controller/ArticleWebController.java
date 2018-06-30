package com.yoga.content.article.controller;

import com.mongodb.DBObject;
import com.yoga.content.article.dto.AddRelatedDto;
import com.yoga.content.article.dto.EditDto;
import com.yoga.content.article.dto.FrameDto;
import com.yoga.content.article.dto.WebListDto;
import com.yoga.content.article.model.EditElement;
import com.yoga.content.article.service.ArticleService;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.content.property.cache.PropertyCache;
import com.yoga.content.property.service.PropertyService;
import com.yoga.content.template.model.TemplateField;
import com.yoga.content.template.service.TemplateFieldService;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.NumberUtil;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.tenant.setting.service.SettingService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cms/article")
public class ArticleWebController extends BaseWebController {
    @Autowired
    private TemplateService templateService = null;
    @Autowired
    private ColumnService columnService = null;
    @Autowired
    private TemplateFieldService fieldService = null;
    @Autowired
    private ArticleService articleService = null;
    @Autowired
    private PropertyService optionenService = null;
    @Autowired
    private PropertyCache optionenCache = null;
    @Autowired
    private DepartmentService departmentService = null;
    @Autowired
    private SettingService settingService = null;
    @Autowired
    private PropertiesService propertiesService = null;

    @RequiresAuthentication
    @RequestMapping("")
    public String frameList(HttpServletRequest request, ModelMap model, TenantPage page, @Valid FrameDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getColumnId() == 0 && StrUtil.isNotBlank(dto.getColumnCode())) {
            Column column = columnService.getColumn(dto.getTid(), dto.getColumnCode());
            if (column != null) dto.setColumnId(column.getId());
            dto.setColumnCode(null);
        }
        if (dto.getParentId() == 0 && StrUtil.isNotBlank(dto.getParentCode())) {
            Column column = columnService.getColumn(dto.getTid(), dto.getParentCode());
            if (column != null) dto.setParentId(column.getId());
            dto.setParentCode(null);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        List<Column> columns = columnService.columnTreeOf(dto.getTid(), dto.getParentId());
        model.put("columns", columns);
        model.put("query", StrUtil.map2Query(params));
        model.put("param", params);
        model.put("page", new CommonPage());
        if (dto.getColumnId() == 0 && columns.size() > 0) {
            dto.setColumnId(columns.get(0).getId());
        }
        model.put("columnId", dto.getColumnId());
        return "/article/frame_list";
    }

    @RequiresAuthentication
    @RequestMapping("/alone")
    public String articleFrame(HttpServletRequest request, ModelMap model, TenantPage page, @Valid FrameDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getColumnId() == 0 && StrUtil.isNotBlank(dto.getColumnCode())) {
            try {
                Column column = columnService.getColumn(dto.getTid(), dto.getColumnCode());
                if (column != null) dto.setColumnId(column.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dto.setColumnCode(null);
        }
        if (dto.getColumnId() == 0 && StrUtil.isBlank(dto.getColumnCode())) {
            return frameList(request, model, page, dto, bindingResult);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        List<Column> columns = columnService.columnTreeOf(dto.getTid(), null);
        model.put("columns", columns);
        model.put("query", StrUtil.map2Query(params));
        model.put("param", params);
        model.put("page", new CommonPage());
        if (dto.getColumnId() == 0 && columns.size() > 0) {
            dto.setColumnId(columns.get(0).getId());
        }
        model.put("columnId", dto.getColumnId());
        return "/article/frame_alone";
    }

    @RequiresAuthentication
    @RequestMapping("/list")
    public String listArticle(HttpServletRequest request, ModelMap model, TenantPage page, @Valid WebListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, Object> params = new HashMap<>();
        PageList<DBObject> articles = null;
        if (dto.getColumnId() == null && StrUtil.isNotBlank(dto.getColumnCode())) {
            Column column = columnService.getColumn(dto.getTid(), dto.getColumnCode());
            dto.setColumnId(column.getId());
        }
        if (dto.getColumnId() != null && dto.getColumnId() != 0) {
            Map<String, String> conditions = new HashMap<String, String>();
            conditions.put("(N)columnId", dto.getColumnId().toString());
            if (StrUtil.isNotBlank(dto.getName())) {
                conditions.put(".title", dto.getName());
            }
            articles = articleService.find(dto.getTid(), false, conditions, new String[]{"online", "sort"},
                    new String[]{"-sort", "-date"}, page.getPageIndex(), page.getPageSize());
        } else {
            articles = new PageList<>();
        }
        params.put("name", dto.getName());
        params.put("columnId", dto.getColumnId());
        params.put("columnCode", dto.getColumnCode());
        params.put("articleId", dto.getArticleId());
        params.put("alone", dto.isAlone());
        model.put("param", params);
        model.put("page", articles.getPage());
        model.put("articles", articles);
        return "/article/list";
    }

    @RequiresAuthentication
    @RequestMapping("/edit")
    public String editArticle(HttpServletRequest request, ModelMap model, @Valid EditDto dto, BindingResult bindingResult) {
        DBObject content = null;
        if (StrUtil.isNotBlank(dto.getId())) {
            content = articleService.detail(dto.getTid(), dto.getId());
        }
        Column column = columnService.getColumn(dto.getTid(), dto.getColumnId());
        if (column.getTemplateId() == 0) throw new BusinessException("该栏目尚未绑定模板，无法添加文章！");
        List<TemplateField> fields = fieldService.findFields(dto.getTid(), column.getTemplateId(), null, null, true);
        Map<String, Object> searchValue = new HashMap<>();
        model.put("columnId", column.getId());
        model.put("columnCode", column.getCode());
        model.put("uploadPath", propertiesService.getZuiUploadUrl());
        model.put("fineUploadPath", propertiesService.getFineUploadUrl());
        model.put("content", content);
        model.put("alone", dto.isAlone());

        List<EditElement> elements = new ArrayList<>();
        for (TemplateField field : fields) {
            EditElement element = new EditElement(field.getType(), field.getName(), field.getCode(), field.getHint(), field.getPlaceholder());
            if (content != null) {
                element.setValue(content.get(element.getCode()));
            }
            switch (element.getFieldType()) {
                case CHOICE: {
                    if (!StrUtil.isBlank(field.getParam())) {
                        element.setOptions(field.getParam().split(","));
                    }
                    break;
                }
                case DROPDOWN: {
                    element.setValues(optionenService.allValues(dto.getTid(), element.getCode()));
                    break;
                }
                case IMAGES: {
                    if (element.getValue() != null && !StrUtil.isBlank(element.getValue().toString())) {
                        element.setValue(element.getValue().toString().split("\\*"));
                    }
                    break;
                }
                case GPS: {
                    if (content != null) {
                        Object x = content.get(element.getCode() + "x");
                        Object y = content.get(element.getCode() + "y");
                        if (x != null) element.setLongitude(NumberUtil.doubleValue(x.toString()));
                        if (y != null) element.setLatitude(NumberUtil.doubleValue(y.toString()));
                    }
                    break;
                }
                case DOCUMENT: {
                    String param = field.getParam();
                    if (StrUtil.isNotBlank(param)) {
                        String[] ext = param.split(",");
                        param = "";
                        for (String e : ext) {
                            param += ",'" + e + "'";
                        }
                        if (param.length() > 0) param = param.substring(1);
                    }
                    element.setFileType(param);
                    break;
                }
                case ARTICLE: {
                    if (content != null) {
                        String ids = (String) content.get(element.getCode());
                        if (StrUtil.isNotBlank(ids)) {
                            List<DBObject> articles = new ArrayList<>();
                            for (String id : ids.split(",")) {
                                articles.add(articleService.detail(dto.getTid(), id));
                            }
                            element.setArticles(articles);
                        }
                    }
                    break;
                }
            }
            elements.add(element);
        }
        model.put("articleId", dto.getId());
        model.put("elements", elements);
        return "/article/edit";
    }

    @RequiresAuthentication
    @RequestMapping("/related")
    public String sss(HttpServletRequest request, ModelMap model, TenantPage page, @Valid AddRelatedDto dto, BindingResult bindingResult) {
        Map<String, String> conditions = new HashMap<String, String>() {
            {
                put(".title", dto.getFilter());
            }
        };
        PageList<DBObject> contentLsit = articleService.find(dto.getTid(), false, conditions, null, new String[]{"-date"}, page.getPageIndex(), page.getPageSize());
        model.put("contentLsit", contentLsit);
        model.put("filter", dto.getFilter());
        model.put("fieldCode", dto.getFieldCode());
        return "/article/related";
    }
}
