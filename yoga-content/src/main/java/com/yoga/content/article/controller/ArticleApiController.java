package com.yoga.content.article.controller;

import com.mongodb.DBObject;
import com.yoga.content.article.dto.*;
import com.yoga.content.article.service.ArticleService;
import com.yoga.content.article.service.HotKeywordService;
import com.yoga.content.enums.FavorType;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.user.model.Favor;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.FavorService;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.util.*;

@Explain(value = "CMS文章管理", module = "cms_article")
@RestController
@RequestMapping("/api/cms/article")
public class ArticleApiController extends BaseApiController {

    @Autowired
    private FavorService favorService = null;
    @Autowired
    private ArticleService articleService = null;
    @Autowired
    private UserService userService = null;
    @Autowired
    private HotKeywordService hotKeywordService = null;


    @Explain("增加收藏文章（登录用户）")
    @RequiresAuthentication
    @RequestMapping("/favor/add")
    public CommonResult addFavor(HttpServletRequest request, @Valid AddFavorDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        long userId = user.getId();
        favorService.addFavor(dto.getTid(), userId, FavorType.Article_Favor, dto.getArticleId(), dto.getArticleTitle(), dto.getTemplateCode(), null, null, dto.getPoster(), dto.getSummary());
        return new CommonResult();
    }

    @Explain("删除收藏文章（登录用户）")
    @RequiresAuthentication
    @RequestMapping("/favor/delete")
    public CommonResult delFavor(HttpServletRequest request, @Valid DelFavorDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getArticleId() == null && dto.getFavorId() == null) {
            throw new BusinessException("请指定文章ID或者收藏编码！");
        }
        if (dto.getFavorId() != null) {
            favorService.delFavor(dto.getFavorId());
        } else {
            favorService.delFavorByObject(FavorType.Article_Favor, dto.getArticleId());
        }
        return new CommonResult();
    }

    @Explain("判断文章是否已经被收藏（登录用户）")
    @RequiresAuthentication
    @RequestMapping("/favor/is")
    public CommonResult isFavor(HttpServletRequest request, @Valid DelFavorDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        long userId = user.getId();
        boolean isFavor = favorService.isFavor(userId, FavorType.Article_Favor, dto.getArticleId());
        return new CommonResult(isFavor);
    }

    @Explain("收藏文章列表（登录用户）")
    @RequiresAuthentication
    @RequestMapping("/favor/list")
    public CommonResult allFavor(HttpServletRequest request, TenantPage page, @Valid AllFavorDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        long userId = user.getId();
        Page<Favor> favors = favorService.findFavor(dto.getTid(), userId, FavorType.Article_Favor, null, dto.getTitle(), dto.getFilter(), null, null, page.getPageIndex(), page.getPageSize());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<Favor>() {
            @Override
            public void convert(Favor item, MapConverter.MapItem<String, Object> map) {
                map
                        .set("id", item.getId())
                        .set("articleId", item.getObjectId())
                        .set("title", item.getTitle())
                        .set("templateCode", item.getParam1())
                        .set("poster", item.getParam4())
                        .set("summary", item.getParam5())
                        .set("date", item.getFavorDate());
            }
        }).build(favors), favors);
    }

    @Explain("新建文章")
    @RequiresPermissions("cms_article.add")
    @RequestMapping("/add")
    public CommonResult addArticle(MultipartHttpServletRequest request, @Valid AddDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Collection<Part> parts = null;
        parts = request.getParts();
        Map<String, String> fields = new HashMap<>();
        for (Part part : parts) {
            String name = part.getName();
            byte[] bytes = new byte[(int) part.getSize()];
            part.getInputStream().read(bytes);
            String value = fields.get(name);
            if (value == null) value = new String(bytes);
            else value += "*" + new String(bytes);
            fields.put(name, value);
        }
        String title = fields.get("title");
        if (StrUtil.isBlank(title)) throw new BusinessException("请输入文章标题");
        articleService.addContent(dto.getTid(), dto.getColumnId(), dto.getTemplateCode(), dto.getTitle(), userService.getLoginInfo().getId(), userService.getLoginInfo().getFullname(), fields);
        return new CommonResult();
    }

    @Explain("修改文章")
    @RequiresPermissions("cms_article.update")
    @RequestMapping("/update")
    public CommonResult updateArticle(MultipartHttpServletRequest request, @Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        try {
            Collection<Part> parts = null;
            parts = request.getParts();
            Map<String, String> fields = new HashMap<>();
            for (Part part : parts) {
                String name = part.getName();
                byte[] bytes = new byte[(int) part.getSize()];
                part.getInputStream().read(bytes);
                String value = fields.get(name);
                if (value == null) value = new String(bytes);
                else value += "*" + new String(bytes);
                fields.put(name, value);
                if (name.compareToIgnoreCase("title") == 0 && value.trim().isEmpty()) {
                    throw new BusinessException("请输入文章标题");
                }
            }
            articleService.updateContent(dto.getTid(), dto.getId(), fields);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        return new CommonResult();
    }

    private void normalizeContentList(Iterable<DBObject> objects) {
        for (DBObject dbObject : objects) {
            ObjectId id = (ObjectId) dbObject.get("_id");
            if (id != null) {
                dbObject.put("id", id.toString());
            }
            dbObject.removeField("_id");
            dbObject.removeField("_class");
        }
    }

    @Explain("文章列表")
    @RequestMapping("/list")
    public CommonResult ListArticle(HttpServletRequest request, TenantPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<DBObject> objects = articleService.find(dto.getTid(), dto.isOnlineOnly(), dto.getColumnId(), dto.getColumnCode(), null, null, null, null, null, dto.getFields(), page.getPageIndex(), page.getPageSize());
        normalizeContentList(objects);
        return new CommonResult(objects, objects.getPage());
    }

    @Explain("查找文章")
    @RequestMapping("/find")
    public CommonResult findArticle(HttpServletRequest request, TenantPage page, @Valid FindDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, String[]> parameters = request.getParameterMap();
        Map<String, String> conditions = new HashMap<>();
        String[] sorts = null;
        String[] keywords = null;
        for (Map.Entry<String, String[]> param : parameters.entrySet()) {
            if (param.getValue() != null && param.getValue().length > 0) {
                String key = param.getKey();
                String[] value = param.getValue();
                if (key.length() < 1) continue;
                if (value.length < 1) continue;
                if (key.equals("pageIndex")) continue;
                if (key.equals("pageSize")) continue;
                if (key.equals("tid")) continue;
                else if (key.equals("fields")) continue;
                if (key.equals("keyword")) keywords = value;
                else if (key.equals("sort")) sorts = value;
                else conditions.put(key, value[0]);
            }
        }
        if (keywords != null) {
            hotKeywordService.hitKeywords(keywords);
        }
        PageList<DBObject> objects = articleService.find(dto.getTid(), dto.isOnlineOnly(), conditions, dto.getFields(), sorts, page.getPageIndex(), page.getPageSize());
        normalizeContentList(objects);
        return new CommonResult(objects, objects.getPage());
    }

    @Explain("符合条件的文章数量")
    @RequestMapping("/count")
    public CommonResult countOfContent(HttpServletRequest request, @Valid FindDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, String[]> parameters = request.getParameterMap();
        Map<String, String> conditions = new HashMap<>();
        for (Map.Entry<String, String[]> param : parameters.entrySet()) {
            if (param.getValue() != null && param.getValue().length > 0) {
                String key = param.getKey();
                String[] value = param.getValue();
                if (key.length() < 1) continue;
                if (value.length < 1) continue;
                if (key.equals("tid")) continue;
                if (key.equals("pageIndex")) continue;
                if (key.equals("pageSize")) continue;
                if (key.equals("fields")) continue;
                else if (key.equals("sort")) continue;
                else conditions.put(key, value[0]);
            }
        }
        long count = articleService.countOf(dto.getTid(), dto.isOnlineOnly(), conditions);
        return new CommonResult(count);
    }

    @Explain("文章详情")
    @RequestMapping("/detail")
    public CommonResult contentDetail(HttpServletRequest request, @Valid DetailDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        DBObject dbObject = articleService.detail(dto.getTid(), dto.getArticleId());
        ObjectId id = (ObjectId) dbObject.get("_id");
        if (id != null) {
            dbObject.put("id", id.toString());
        }
        dbObject.removeField("_id");
        dbObject.removeField("_class");
        return new CommonResult(dbObject);
    }

    @Explain("多文章详情")
    @RequestMapping("/details")
    public CommonResult contentDetails(HttpServletRequest request, @Valid DetailsDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<DBObject> dbObjects = articleService.details(dto.getTid(), dto.getArticleIds());
        normalizeContentList(dbObjects);
        return new CommonResult(dbObjects);
    }

    @Explain("删除文章")
    @RequiresPermissions("cms_article.del")
    @RequestMapping("/delete")
    public CommonResult removeContent(HttpServletRequest request, @Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        articleService.removeContent(dto.getTid(), dto.getId());
        return new CommonResult();
    }

    @Explain("热点关键字列表")
    @RequestMapping("/hotest/keywords")
    public CommonResult hotestKeywords(HttpServletRequest request, @Valid HotKeywordDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Set<String> keywords = hotKeywordService.hotestKeywords(dto.getCount());
        return new CommonResult(keywords);
    }

    @Explain("切换文章上下线")
    @RequiresPermissions("cms_article.update")
    @RequestMapping("/online")
    public CommonResult updownLine(HttpServletRequest request, @Valid OnlineDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        articleService.setOnline(dto.getTid(), dto.getId(), dto.getOnline());
        return new CommonResult();
    }

    @Explain("设置文章排序")
    @RequiresPermissions("cms_article.update")
    @RequestMapping("/sort")
    public CommonResult setSort(HttpServletRequest request, @Valid SortDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        articleService.setSort(dto.getTid(), dto.getId(), dto.getSort());
        return new CommonResult();
    }
}
