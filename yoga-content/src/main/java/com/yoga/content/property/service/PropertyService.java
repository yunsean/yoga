package com.yoga.content.property.service;

import com.yoga.content.template.enums.FieldType;
import com.yoga.content.property.cache.PropertyCache;
import com.yoga.content.property.model.Property;
import com.yoga.content.property.repo.PropertyRepository;
import com.yoga.content.template.repo.TemplateFieldRepository;
import com.yoga.content.sequence.SequenceNameEnum;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PropertyService extends BaseService {

    @Autowired
    private PropertyRepository propertyRepository = null;
    @Autowired
    private TemplateFieldRepository fieldRepository = null;
    @Autowired
    private PropertyCache propertyCache = null;

    public Map<String, Property> propertyMap(long tenantId) {
        return propertyCache.propertyMap(tenantId);
    }

    public void addProperty(long tenantId, String code, String name) {
        if (StrUtil.isBlank(code)) throw new BusinessException("必须指定选项编码！");
        if (propertyRepository.countByTenantIdAndCode(tenantId, code) > 0) throw new BusinessException("已经存在同名编码的选项！");
        Property property = new Property(tenantId, name, 0, code);
        property.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_CMS_PROPERTY_ID));
        propertyRepository.save(property);
        propertyCache.cleanCache(tenantId);
    }

    public void addValue(long tenantId, long parentId, String name, String code) {
        Property parent = propertyRepository.findFirstByTenantIdAndId(tenantId, parentId);
        if (parent == null) throw new BusinessException("未找到对应编码的选项！");
        Property property = new Property(tenantId, name, parentId, code);
        property.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_CMS_PROPERTY_ID));
        propertyRepository.save(property);
        propertyCache.cleanCache(tenantId);
    }

    public void delProperty(long tenantId, long id) {
        Property optionen = propertyRepository.findFirstByTenantIdAndId(tenantId, id);
        if (optionen == null) throw new BusinessException("未找到该选项或选项值！");
        if (optionen.getParentId() == 0 && StrUtil.isNotBlank(optionen.getCode())) {
            long count = fieldRepository.countByTenantIdAndTypeAndCode(tenantId, FieldType.DROPDOWN, optionen.getCode());
            if (count > 0) throw new BusinessException("当前选项正在使用中，无法被删除！");
        }
        propertyRepository. deletePropertyById(id, true);
        propertyCache.cleanCache(tenantId);
    }

    public Iterable<Property> allProperties(long tenantId) {
        Map<String, Property> properties = propertyCache.propertyMap(tenantId);
        return properties.values();
    }

    public List<Property> allValues(long tenantId, String code) {
        Map<String, Property> properties = propertyCache.propertyMap(tenantId);
        Property property = properties.get(code);
        if (property == null) throw new BusinessException("未找到对应编码的选项！");
        if (property.getChildren() == null) {
            return new ArrayList<>();
        } else {
            return property.getChildren();
        }
    }

    public void update(long tenantId, long id, String name, String code, Long propertyId) {
        Property property = propertyRepository.findFirstByTenantIdAndId(tenantId, id);
        if (property == null) throw new BusinessException("未找到对应编码的选项或选项值！");
        if (propertyId != null && propertyId != 0) {
            long count = propertyRepository.countByTenantIdAndId(tenantId, propertyId);
            if (count < 1) throw new BusinessException("选项不存在！");
        }
        if (name != null) property.setName(name);
        if (code != null) property.setCode(code);
        if (propertyId != null) property.setParentId(propertyId);
        propertyRepository.save(property);
        propertyCache.cleanCache(tenantId);
    }
}
