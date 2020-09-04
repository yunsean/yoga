package com.yoga.content.property.service;

import com.yoga.content.column.model.Column;
import com.yoga.content.property.mapper.PropertyMapper;
import com.yoga.content.template.enums.FieldType;
import com.yoga.content.property.model.Property;
import com.yoga.content.template.mapper.TemplateFieldMapper;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("cmsPropertyService")
@LoggingPrimary(module = PropertyService.ModuleName, name = "CMS属性管理")
public class PropertyService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private PropertyMapper propertyMapper;
    @Autowired
    private TemplateFieldMapper fieldMapper;

    public final static String ModuleName = "cms_property";
    @Override
    public String getPrimaryInfo(Object primaryId) {
        Property property = propertyMapper.selectByPrimaryKey(primaryId);
        if (property == null) return null;
        return property.getName();
    }

    @Logging(module = ModuleName, description = "添加属性", primaryKeyIndex = -1, argNames = "租户ID，属性编码，属性名称，父ID")
    public long add(long tenantId, String code, String name, long parentId, String poster) {
        if (StringUtil.isBlank(code)) throw new BusinessException("必须指定选项编码！");
        if (new MapperQuery<>(Property.class)
            .andEqualTo("tenantId", tenantId)
            .andEqualTo("code", code)
            .count(propertyMapper) > 0) throw new BusinessException("已经存在同名编码的选项！");
        Property property = new Property(tenantId, code, name, parentId, poster);
        propertyMapper.insert(property);
        return property.getId();
    }
    @Logging(module = ModuleName, description = "删除属性", primaryKeyIndex = 1, argNames = "租户ID，")
    public void delete(long tenantId, long id) {
        Property optionen = propertyMapper.selectByPrimaryKey(id);
        if (optionen == null || optionen.getTenantId() != tenantId) throw new BusinessException("未找到对应编码的选项！");
        if ((optionen.getParentId() == null || optionen.getParentId() == 0) && StringUtil.isNotBlank(optionen.getCode())) {
            long count = fieldMapper.countOfProperty(tenantId, FieldType.DROPDOWN.getCode(), optionen.getCode());
            if (count > 0) throw new BusinessException("当前选项正在使用中，无法被删除！");
        }
        propertyMapper.deleteByPrimaryKey(id);
    }
    @Logging(module = ModuleName, description = "修改属性", primaryKeyIndex = 1, argNames = "租户ID，，属性编码，属性名称，父ID")
    public void update(long tenantId, long id, String name, String code, Long parentId, String poster) {
        Property property = propertyMapper.selectByPrimaryKey(id);
        if (property == null || property.getTenantId() != tenantId) throw new BusinessException("未找到对应编码的选项！");
        if (parentId != null && parentId != 0) {
            Property parent = propertyMapper.selectByPrimaryKey(parentId);
            if (parent == null || parent.getTenantId() != tenantId) throw new BusinessException("选项不存在！");
            property.setParentId(parentId);
        }
        if (StringUtil.isNotBlank(code) && !code.equals(property.getCode())) {
            if (new MapperQuery<>(Property.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("code", code)
                    .count(propertyMapper) > 0) throw new BusinessException("已经存在同名编码的选项！");
            property.setCode(code);
        }
        if (StringUtil.isNotBlank(name)) property.setName(name);
        if (poster != null) property.setPoster(poster);
        propertyMapper.updateByPrimaryKey(property);
    }
    public List<Property> childrenOf(long tenantId, long parentId, boolean includeSelf) {
        return childrenOf(tenantId, parentId, includeSelf, true);
    }
    public List<Property> childrenOf(long tenantId, long parentId, boolean includeSelf, boolean showAsTree) {
        List<Property> properties;
        if (parentId == 0) properties = new MapperQuery<>(Property.class)
                .andEqualTo("tenantId", tenantId)
                .query(propertyMapper);
        else properties = propertyMapper.childrenOf(tenantId, parentId, includeSelf);
        if (showAsTree) return composeColumn(properties, true);
        else return properties;
    }
    public List<Property> childrenOf(long tenantId, String parentCode, boolean includeSelf) {
        return childrenOf(tenantId, parentCode, includeSelf, true);
    }
    public List<Property> childrenOf(long tenantId, String parentCode, boolean includeSelf, boolean showAsTree) {
        List<Property> properties;
        if (StringUtil.isBlank(parentCode)) properties = new MapperQuery<>(Property.class)
                .andEqualTo("tenantId", tenantId)
                .query(propertyMapper);
        else properties = propertyMapper.childrenOfCode(tenantId, parentCode, includeSelf);
        if (showAsTree) return composeColumn(properties, true);
        else return properties;
    }
    public List<Property> list(long tenantId, Long parentId) {
        if (parentId == null) return propertyMapper.selectAll();
        else return new MapperQuery<>(Property.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("parentId", parentId)
                .query(propertyMapper);
    }
    public Property get(long tenantId, String code) {
        Property property = new MapperQuery<>(Property.class)
                .andEqualTo("code", code)
                .queryFirst(propertyMapper);
        if (property == null || property.getTenantId() != tenantId) throw new BusinessException("选项不存在！");
        return property;
    }
    public Property get(long tenantId, long id) {
        Property property = propertyMapper.selectByPrimaryKey(id);
        if (property == null || property.getTenantId() != tenantId) throw new BusinessException("选项不存在！");
        return property;
    }
    public List<Property> get(long tenantId, List<Long> ids) {
        if (ids == null || ids.size() < 1) return new ArrayList<>();
        return propertyMapper.listByIds(tenantId, ids);
    }

    private List<Property> composeColumn(List<Property> columns, boolean sort) {
        if (columns == null) return null;
        if (sort) columns.sort(Comparator.comparing(Property::getName));
        Map<Long, Property> mapColumns = columns.stream().collect(Collectors.toMap(Property::getId, b-> b));
        Iterator<Map.Entry<Long, Property>> it = mapColumns.entrySet().iterator();
        List<Property> result = new ArrayList<>();
        while (it.hasNext()) {
            Property self = it.next().getValue();
            Property parent = mapColumns.get(self.getParentId());
            if (parent != null) parent.addChild(self);
            else result.add(self);
        }
        return result;
    }
}
