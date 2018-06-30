package com.yoga.content.layout.service;

import com.yoga.content.layout.cache.LayoutCache;
import com.yoga.content.layout.model.Layout;
import com.yoga.content.layout.repo.LayoutRepository;
import com.yoga.content.enums.LayoutType;
import com.yoga.content.sequence.SequenceNameEnum;
import com.yoga.content.template.model.Template;
import com.yoga.content.template.repo.TemplateRepository;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class LayoutService extends BaseService {
    @Autowired
    private LayoutRepository layoutRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private LayoutCache layoutCache;

    public void add(long tenantId, long templateId, LayoutType type, String title, String image, String[] fields, String html, String css, String js, String[] accessories) {
        Template template = templateRepository.findOne(templateId);
        if (template == null || template.getTenantId() != tenantId) throw new BusinessException("模板不存在！");
        String strFields = StringUtils.join(fields, "*");
        Layout layout = new Layout(tenantId, templateId, type, image, title, strFields, html, css, js, StrUtil.array2String(accessories));
        layout.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_CMS_LAYOUT_ID));
        layoutRepository.save(layout);
        layoutCache.clearCache(tenantId);
    }

    public void delete(long tenantId, long layoutId) {
        Layout layout = layoutRepository.findOne(layoutId);
        if (layout == null || layout.getTenantId() != tenantId) throw new BusinessException("布局不存在！");
        layoutRepository.delete(layout);
        layoutCache.clearCache(tenantId);
    }

    public void update(long tenantId, long layoutId, String title, String image, String[] fields, String html, String css, String js, String[] accessories) {
        Layout layout = layoutRepository.findOne(layoutId);
        if (layout == null || layout.getTenantId() != tenantId) throw new BusinessException("布局不存在！");
        if (StrUtil.isNotBlank(title)) layout.setTitle(title);
        if (StrUtil.isNotBlank(image)) layout.setImage(image);
        if (null != fields && fields.length > 0) layout.setFields(StringUtils.join(fields, "*"));
        if (StrUtil.isNotBlank(html)) layout.setHtml(html);
        if (StrUtil.isNotBlank(css)) layout.setCss(css);
        if (StrUtil.isNotBlank(js)) layout.setJs(js);
        if (accessories != null) layout.setLinkFiles(StrUtil.array2String(accessories));
        layoutRepository.save(layout);
        layoutCache.clearCache(tenantId);
    }

    public List<Layout> find(long tenantId, long templateId, LayoutType layoutType) {
        return layoutRepository.findAll(new Specification<Layout>() {
            @Override
            public Predicate toPredicate(Root<Layout> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.get("tenantId"), tenantId));
                if (templateId != 0) expressions.add(cb.equal(root.get("templateId"), templateId));
                if (layoutType != null) expressions.add(cb.equal(root.get("type"), layoutType));
                return predicate;
            }
        });
    }

    public Layout get(long tenantId, long layoutId) {
        Layout layout = layoutRepository.findOne(layoutId);
        if (layout == null || layout.getTenantId() != tenantId) throw new BusinessException("布局不存在！");
        return layout;
    }
    public Layout get(long layoutId) {
        Layout layout = layoutRepository.findOne(layoutId);
        if (layout == null) throw new BusinessException("布局不存在！");
        return layout;
    }

    public String getFields(long tenantId, long layoutId) {
        return layoutCache.getFields(tenantId, layoutId);
    }
}
