package com.yoga.content.wechat;

import com.yoga.content.column.model.Column;
import com.yoga.content.column.service.ColumnService;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.interfaces.wechat.EventType;
import com.yoga.core.interfaces.wechat.MenuEvent;
import com.yoga.core.interfaces.wechat.WechatAction;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArticleWechatPlugin implements WechatAction {

    @Autowired
    private TemplateService templateService = null;
    @Autowired
    private ColumnService columnService = null;
    @Autowired
    private PropertiesService propertiesService = null;

    @Override
    public String getName() {
        return "网页文章";
    }

    @Override
    public String getCode() {
        return "cms_article";
    }

    @Override
    public EventType[] supportEvent() {
        return new EventType[]{EventType.menu};
    }

    @Override
    public MenuEvent menuEvent() {
        return MenuEvent.view;
    }

    @Override
    public boolean needConfig() {
        return true;
    }

    @Override
    public String showConfig(long tenantId, HttpServletRequest request, HttpServletResponse response, ModelMap model, String config) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("filter", null);
        List<Column> columns = columnService.columnTreeOf(tenantId, null);
        model.put("columns", columns);
        model.put("query", StrUtil.map2Query(params));
        model.put("param", params);
        model.put("templates", templateService.allTemplates(tenantId));
        String baseUrl = propertiesService.getSysBaseurl() + "/cms/layout/show";
        model.put("baseUrl", baseUrl);
        model.put("tenantId", tenantId);
        return "/cms_wechat/article";
    }
}
