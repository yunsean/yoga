package com.yoga.content.article.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.content.article.dto.*;
import com.yoga.content.article.model.EditElement;
import com.yoga.content.article.service.ArticleService;
import com.yoga.content.article.service.HotKeywordService;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.content.property.model.Property;
import com.yoga.content.property.service.PropertyService;
import com.yoga.content.template.enums.FieldType;
import com.yoga.content.template.model.TemplateField;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.ApiResult;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.NumberUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.operator.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Controller("cmsArticleController")
@RequestMapping("/admin/cms/article")
@Api(tags = "CMS文章管理")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private HotKeywordService hotKeywordService;
    @Autowired
    private ColumnService columnService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private PropertyService propertyService;

    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("")
    public String frameList(ModelMap model, @Valid FrameDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getColumnId() == 0 && StringUtil.isNotBlank(dto.getColumnCode())) {
            Column column = columnService.get(dto.getTid(), dto.getColumnCode(), true);
            if (column != null) dto.setColumnId(column.getId());
        }
        if (dto.getParentId() == 0 && StringUtil.isNotBlank(dto.getParentCode())) {
            Column column = columnService.get(dto.getTid(), dto.getParentCode(), true);
            if (column != null) dto.setParentId(column.getId());
            dto.setParentCode(null);
        }
        List<Column> columns = columnService.childrenOf(dto.getTid(), dto.getParentId(), false, true);
        model.put("columns", columns);
        if (dto.getColumnId() == 0 && columns.size() > 0) dto.setColumnId(columns.get(0).getId());
        model.put("param", dto.wrapAsMap());
        return "/admin/cms/article/frame";
    }
    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("/alone")
    public String articleFrame(HttpServletRequest request, ModelMap model, @Valid FrameDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String queryString = request.getQueryString();
        if (StringUtil.isBlank(queryString)) return frameList(model, dto, bindingResult);
        model.put("param", dto.wrapAsMap());
        model.put("queryString", queryString + "&alone=true");
        return "/admin/cms/article/alone";
    }
    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("/list")
    public String listArticle(HttpServletRequest request, ModelMap model, CommonPage page, @Valid WebListDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, String> conditions = readParameters(request);
        if (dto.getColumnId() != null) {
            conditions.remove("columId");
            if (dto.getColumnId() != 0L) conditions.put("(N)columnId", String.valueOf(dto.getColumnId()));
        }
        if (dto.getColumnCode() != null) {
            Column column = columnService.get(dto.getTid(),dto.getColumnCode(), true);
            if (column != null) {
                conditions.remove("columnCode");
                conditions.put("(N)columnId", String.valueOf(column.getId()));
                dto.setColumnId(column.getId());
            }
        }
        if (conditions.containsKey("name")) {
            String title = conditions.get("name");
            conditions.remove("name");
            if (StringUtil.isNotBlank(title)) conditions.put(".title", title);
        }
        conditions.remove("tid");
        conditions.remove("alone");
        PageInfo<Document> articles = articleService.find(dto.getTid(), false, conditions, new String[]{"online", "sort"},
                new String[]{"-sort", "-date"}, page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("page", new CommonPage(articles));
        model.put("articles", articles.getList());
        return "/admin/cms/article/list";
    }
    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("/edit")
    public String editArticle(ModelMap model, @Valid EditDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Document content = null;
        if (StringUtil.isNotBlank(dto.getArticleId())) content = articleService.get(dto.getTid(), dto.getArticleId());
        Column column = null;
        if (content != null && NumberUtil.longValue(content.get("columnId").toString()) != 0) column = columnService.get(dto.getTid(), NumberUtil.longValue(content.get("columnId").toString()));
        else if (dto.getColumnId() != null) column = columnService.get(dto.getTid(), dto.getColumnId());
        if (column == null) throw new IllegalArgumentException("请指定需要编辑的文章或者新建文件的栏目！");
        if (column.getTemplateId() == null || column.getTemplateId() == 0) throw new BusinessException("该栏目尚未绑定模板，无法添加文章！");
        List<TemplateField> fields = templateService.listField(column.getTemplateId(), null, null, true);
        model.put("columnId", column.getId());
        model.put("columnCode", column.getCode());
        model.put("templateId", column.getTemplateId());
        model.put("templateCode", column.getTemplateCode());
        model.put("param", dto.wrapAsMap());
        List<EditElement> elements = new ArrayList<>();
        for (TemplateField field : fields) {
            EditElement element = new EditElement(field.getType(), field.getName(), field.getCode(), field.getHint(), field.getPlaceholder());
            if (content != null) element.setValue(content.get(element.getCode()));
            switch (element.getFieldType()) {
                case CHECKBOX: {
                    if (content != null) {
                        Object values = content.get(element.getCode());
                        if (values instanceof List<?>) {
                            element.setCheckeds(((List<?>) values).stream().map(String::valueOf).collect(Collectors.toList()));
                        }
                    }
                }
                case CHOICE:{
                    if (!StringUtil.isBlank(field.getParam())) {
                        Map<String, String> options = new HashMap<>();
                        Arrays.stream(field.getParam().split("\\|")).forEach(it-> {
                            String[] parts = it.split(":");
                            if (parts.length > 1) options.put(parts[0], parts[1]);
                            else options.put(parts[0], parts[0]);
                        });
                        element.setOptions(options);
                    }
                    break;
                }
                case CHECKDOWN: {
                    if (content != null) {
                        Object values = content.get(element.getCode());
                        if (values instanceof List<?>) {
                            element.setCheckeds(((List<?>) values).stream().map(String::valueOf).collect(Collectors.toList()));
                        }
                    }
                    Property property = propertyService.get(dto.getTid(), element.getCode());
                    element.setValues(propertyService.childrenOf(dto.getTid(), property.getId(), false, false));
                    break;
                }
                case DROPDOWN: {
                    Property property = propertyService.get(dto.getTid(), element.getCode());
                    element.setValues(propertyService.childrenOf(dto.getTid(), property.getId(), false, true));
                    break;
                }
                case IMAGES: {
                    element.setValue(null);
                    if (content != null) {
                        Object values = content.get(element.getCode());
                        if (values instanceof List<?>) {
                            element.setValue(((List<?>) values).stream().map(String::valueOf).collect(Collectors.toList()));
                        }
                    }
                    break;
                }
                case GPS: {
                    if (content != null) {
                        Object x = content.get(element.getCode() + "_x");
                        Object y = content.get(element.getCode() + "_y");
                        if (x != null) element.setLongitude(NumberUtil.doubleValue(x.toString()));
                        if (y != null) element.setLatitude(NumberUtil.doubleValue(y.toString()));
                    }
                    break;
                }
                case DOCUMENT: {
                    String param = field.getParam();
                    element.setFileType(param);
                    if (content != null) {
                        Object values = content.get(element.getCode());
                        if (values instanceof List<?>) {
                            element.setFiles(((List<?>) values).stream().map(String::valueOf).collect(Collectors.toList()));
                        }
                    }
                    break;
                }
                case ARTICLE: {
                    if (content != null) {
                        Object values = content.get(element.getCode());
                        if (values instanceof List<?>) {
                            List<String> ids = ((List<?>) values).stream().map(String::valueOf).collect(Collectors.toList());
                            List<Document> articles = articleService.get(dto.getTid(), ids, null);
                            element.setArticles(articles);
                        }
                    }
                    break;
                }
                case HIDDEN: {
                    element.setValue(field.getParam());
                }
            }
            elements.add(element);
        }
//        Optional<EditElement> summaryElement = elements.stream().filter(it-> "summary".equalsIgnoreCase(it.getCode())).findAny();
//        if (!summaryElement.isPresent()) {
//            EditElement element = new EditElement(FieldType.SECTION, "文章简介", "summary", "如不填写，将自动使用文章内容填充", null);
//            if (content != null) element.setValue(content.getString("summary"));
//            elements.add(0, element);
//        }
        Optional<EditElement> titleElement = elements.stream().filter(it-> "title".equalsIgnoreCase(it.getCode())).findAny();
        if (!titleElement.isPresent()) {
            EditElement element = new EditElement(FieldType.TEXT, "文章标题", "title", null, null);
            if (content != null) element.setValue(content.getString("title"));
            elements.add(0, element);
        }
        Optional<EditElement> onlineElement = elements.stream().filter(it-> "online".equalsIgnoreCase(it.getCode())).findAny();
        if (onlineElement.isPresent()) {
            elements.remove(onlineElement.get());
            elements.add(onlineElement.get());
        } else  {
            EditElement element = new EditElement(FieldType.CHOICE, "是否上线", "(N)online", null, null);
            element.setOptions(new HashMap<String, String>(){{
                put("0", "暂不上线");
                put("1", "立即上线");
            }});
            if (content != null) element.setValue(String.valueOf(NumberUtil.doubleValue(String.valueOf(content.get("online"))) > 0.5 ? 1 : 0));
            else element.setValue("0");
            elements.add(element);
        }
        model.put("articleId", dto.getArticleId());
        model.put("elements", elements);
        return "/admin/cms/article/edit";
    }
    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("/related")
    public String related(ModelMap model, CommonPage page, @Valid AddRelatedDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, String> conditions = new HashMap<>();
        conditions.put(".title", dto.getFilter());
        page.setPageSize(10);
        PageInfo<Document> articles = articleService.find(dto.getTid(), false, conditions, null, new String[]{"-date"}, page.getPageIndex(), page.getPageSize());
        model.put("articles", articles.getList());
        model.put("page", new CommonPage(articles));
        model.put("param", dto.wrapAsMap());
        return "/admin/cms/article/related";
    }

    @ResponseBody
    @ApiOperation("新建文章")
    @RequiresPermissions("cms_article.add")
    @PostMapping("/add.json")
    public ApiResult addArticle(MultipartHttpServletRequest request, @ModelAttribute @Valid AddDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        Map<String, ? extends Object> fields = readParts(request);
        String title = fields.get("title").toString();
        if (StringUtil.isBlank(title)) throw new BusinessException("请输入文章标题");
        articleService.add(dto.getTid(), dto.getColumnId(), dto.getTemplateCode(), dto.getTitle(), user.getId(), user.getNickname(), fields);
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("修改文章")
    @RequiresPermissions("cms_article.update")
    @PostMapping("/update.json")
    public ApiResult updateArticle(MultipartHttpServletRequest request, @ModelAttribute @Valid UpdateDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, ? extends Object> fields = readParts(request);
        articleService.update(dto.getTid(), dto.getId(), fields);
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("文章列表")
    @GetMapping("/list.json")
    public ApiResults<Document> ListArticle(CommonPage page, @ModelAttribute @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Document> objects = articleService.find(dto.getTid(), dto.isOnlineOnly(), dto.getColumnId(), dto.getColumnCode(), null, null, null, null, null, dto.getFields(), page.getPageIndex(), page.getPageSize());
        ArticleService.normalizeContent(objects.getList());
        return new ApiResults<>(objects);
    }
    @ResponseBody
    @ApiOperation("查找文章")
    @GetMapping("/find.json")
    public ApiResults<Document> findArticle(HttpServletRequest request, CommonPage page, @ModelAttribute @Valid FindDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, String> conditions = new HashMap<>();
        String[] sorts = null;
        String[] keywords = null;
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, String[]> param : parameters.entrySet()) {
            if (param.getValue() != null && param.getValue().length > 0) {
                String key = param.getKey();
                String[] value = param.getValue();
                if (key.length() < 1) continue;
                if (key.equals("pageIndex")) continue;
                if (key.equals("pageSize")) continue;
                if (key.equals("tid")) continue;
                else if (key.equals("fields")) continue;
                if (key.equals("keyword")) keywords = value;
                else if (key.equals("sort")) sorts = value;
                else conditions.put(key, value[0]);
            }
        }
        if (keywords != null) hotKeywordService.hitKeywords(keywords);
        PageInfo<Document> objects = articleService.find(dto.getTid(), dto.isOnlineOnly(), conditions, dto.getFields(), sorts, page.getPageIndex(), page.getPageSize());
        ArticleService.normalizeContent(objects.getList());
        return new ApiResults<>(objects);
    }
    @ResponseBody
    @ApiOperation("符合条件的文章数量")
    @GetMapping("/count.json")
    public ApiResult<Long> countOfContent(HttpServletRequest request, @ModelAttribute @Valid FindDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, String> conditions = new HashMap<>();
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, String[]> param : parameters.entrySet()) {
            if (param.getValue() != null && param.getValue().length > 0) {
                String key = param.getKey();
                String[] value = param.getValue();
                if (key.length() < 1) continue;
                if (key.equals("tid")) continue;
                if (key.equals("pageSize")) continue;
                if (key.equals("pageIndex")) continue;
                if (key.equals("fields")) continue;
                if (key.equals("sort")) continue;
                conditions.put(key, value[0]);
            }
        }
        long count = articleService.count(dto.getTid(), dto.isOnlineOnly(), conditions);
        return new ApiResult<>(count);
    }
    @ResponseBody
    @ApiOperation("文章详情")
    @GetMapping("/get.json")
    public ApiResult<Document> contentDetail(@ModelAttribute @Valid DetailDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Document document = articleService.get(dto.getTid(), dto.getArticleId());
        ObjectId id = (ObjectId) document.get("_id");
        if (id != null) document.put("id", id.toString());
        document.remove("_id");
        document.remove("_class");
        return new ApiResult<>(document);
    }
    @ResponseBody
    @ApiOperation("多文章详情")
    @GetMapping("/gets.json")
    public ApiResults<Document> contentDetails(@ModelAttribute @Valid DetailsDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Document> documents = articleService.get(dto.getTid(), dto.getArticleIds(), dto.getFields());
        ArticleService.normalizeContent(documents);
        return new ApiResults<>(documents);
    }
    @ResponseBody
    @ApiOperation("删除文章")
    @RequiresPermissions("cms_article.del")
    @DeleteMapping("/delete.json")
    public ApiResult removeContent(@ModelAttribute @Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        articleService.remove(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("热点关键字列表")
    @GetMapping("/hotest/keywords.json")
    public ApiResults<String> hotestKeywords(@ModelAttribute @Valid HotKeywordDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Set<String> keywords = hotKeywordService.hotestKeywords(dto.getCount());
        return new ApiResults<>(keywords);
    }
    @ResponseBody
    @ApiOperation("切换文章上下线")
    @RequiresPermissions("cms_article.update")
    @PostMapping("/online.json")
    public ApiResult updownLine(@ModelAttribute @Valid OnlineDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        articleService.setOnline(dto.getTid(), dto.getId(), dto.getOnline());
        return new ApiResult();
    }
    @ResponseBody
    @ApiOperation("设置文章排序")
    @RequiresPermissions("cms_article.update")
    @PostMapping("/sort.json")
    public ApiResult setSort(@ModelAttribute @Valid SortDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        articleService.setSort(dto.getTid(), dto.getId(), dto.getSort());
        return new ApiResult();
    }

    private Map<String, ? extends Object> readParts(MultipartHttpServletRequest request) throws Exception {
        Collection<Part> parts = request.getParts();
        Map<String, List<String>> fields = new HashMap<>();
        for (Part part : parts) {
            String name = part.getName();
            if (name.equals("tid")) continue;
            if (name.equals("pageSize")) continue;
            if (name.equals("pageIndex")) continue;
            byte[] bytes = new byte[(int) part.getSize()];
            part.getInputStream().read(bytes);
            List<String> values = fields.get(name);
            if (values == null) values = new ArrayList<>();
            values.add(new String(bytes));
            fields.put(name, values);
        }
        return fields;
    }
    private Map<String, String> readParameters(HttpServletRequest request) throws Exception {
        Enumeration<String> parts = request.getParameterNames();
        Map<String, String> fields = new HashMap<>();
        while (parts.hasMoreElements()) {
            String name = parts.nextElement();
            String value = request.getParameter(name);
            if (StringUtil.isBlank(value)) continue;
            if (name.equals("tid")) continue;
            if (name.equals("pageSize")) continue;
            if (name.equals("pageIndex")) continue;
            if (name.equals("alone")) continue;
            fields.put(name, value);
        }
        return fields;
    }
}
