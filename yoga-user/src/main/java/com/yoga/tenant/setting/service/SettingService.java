package com.yoga.tenant.setting.service;

import com.alibaba.fastjson.JSONObject;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.Settables;
import com.yoga.tenant.setting.cache.SettingCache;
import com.yoga.tenant.setting.model.SaveItem;
import com.yoga.tenant.setting.model.SettableItem;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.setting.repo.SettingRepository;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class SettingService extends BaseService {

    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private SettingCache settingCache;

    private static List<SettableItem> settableItems = new ArrayList<>();

    @PostConstruct
    public void loadSettable() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Settable.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Settables.class));
        logger.info("查找配置项服务");
        settableItems.clear();
        Set<BeanDefinition> definitions = provider.findCandidateComponents("com.yoga.**");
        for (BeanDefinition definition : definitions) {
            try {
                Class<?> entityClass = ClassUtils.getClass(definition.getBeanClassName());
                if (entityClass.isAnnotationPresent(Settable.class) || entityClass.isAnnotationPresent(Settables.class)) {
                    Settable[] settables = entityClass.getAnnotationsByType(Settable.class);
                    for (Settable settable : settables) {
                        if (StrUtil.isNotBlank(settable.module())) {
                            settableItems.add(new SettableItem(settable, settable.url()));
                            logger.info("找到配置服务：" + settable.name());
                        }
                    }
                }
                String url = "";
                if (entityClass.getAnnotation(RequestMapping.class) != null) {
                    url += entityClass.getAnnotation(RequestMapping.class).value()[0];
                }
                Method[] methods = entityClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Settable.class) || entityClass.isAnnotationPresent(Settables.class)) {
                        String localUrl = url;
                        if (method.getAnnotation(RequestMapping.class) != null) {
                            localUrl += method.getAnnotation(RequestMapping.class).value()[0];
                        }
                        Settable[] settables = method.getAnnotationsByType(Settable.class);
                        for (Settable settable : settables) {
                            if (StrUtil.isNotBlank(settable.module())) {
                                settableItems.add(new SettableItem(settable, StrUtil.isNotBlank(settable.url()) ? settable.url() : localUrl));
                                logger.info("找到配置服务：" + settable.name());
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Collections.sort(settableItems, (o1, o2) -> {
            if (o1 == null) return -1;
            if (o2 == null) return 1;
            return o1.getName().compareTo(o2.getName());
        });
        logger.info("共找到" + settableItems.size() + "个配置项服务");
    }

    public final static PageList<SettableItem> allSettable(String filter, String[] modules, int pageIndex, int pageSize, boolean systemOnly) {
        List<SettableItem> items = new ArrayList<>();
        int begin = pageIndex * pageSize;
        int end = begin + pageSize;
        int count = 0;
        if (StrUtil.isBlank(filter)) filter = null;
        for (SettableItem item : settableItems) {
            if (item.isSystemOnly() != systemOnly) continue;
            if (filter != null && !item.matchFilter(filter)) continue;
            if (!item.matchModule(modules)) continue;
            if (count >= begin && count < end) {
                items.add(new SettableItem(item));
            }
            count++;
        }
        return new PageList<>(items, new CommonPage(pageIndex, pageSize, count));
    }

    public void save(long tenantId, String module, String key, String value, String showValue) {
        Setting setting = settingRepository.findOneByTenantIdAndModuleAndKey(tenantId, module, key);
        if (setting == null) {
            setting = new Setting();
            setting.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_SETTING_ID));
            setting.setTenantId(tenantId);
            setting.setModule(module);
            setting.setKey(key);
        }
        setting.setValue(value);
        setting.setShowValue(showValue);
        settingRepository.save(setting);
        settingCache.flush(tenantId, module, key);
    }

    @Transactional
    public void save(long tenantId, List<SaveItem> items) {
        for (SaveItem item : items) {
            save(tenantId, item.getModule(), item.getKey(), item.getValue(), item.getValue());
        }
    }

    public Setting get(long tenantId, String module, String key) {
        return settingCache.get(tenantId, module, key);
    }
    public <T> T get(long tenantId, String module, String key, Class<?> clazz) {
        Setting setting = settingCache.get(tenantId, module, key);
        if (setting == null) return null;
        String value = setting.getValue();
        if (value == null) return null;
        if (clazz.isPrimitive()) {
            PropertyEditor editor = PropertyEditorManager.findEditor(clazz);
            editor.setAsText(value);
            return (T) editor.getValue();
        }
        if (clazz == String.class) return (T)value;
        try {
            Method valueOf = clazz.getMethod("valueOf", new Class[] { String.class });
            if (valueOf != null) {
                return (T)valueOf.invoke(null, new Object[]{value});
            }
        } catch (Exception ex) {
        }
        try {
            return (T) JSONObject.parseObject(value, clazz);
        } catch (Exception ex) {
            return null;
        }
    }
    public <T> T get(long tenantId, String module, String key, T defaultValue) {
        Setting setting = settingCache.get(tenantId, module, key);
        if (setting == null) return defaultValue;
        String value = setting.getValue();
        if (value == null) return defaultValue;
        try {
            if (defaultValue.getClass().isPrimitive()) {
                PropertyEditor editor = PropertyEditorManager.findEditor(defaultValue.getClass());
                editor.setAsText(value);
                return (T) editor.getValue();
            }
            Method valueOf = defaultValue.getClass().getMethod("valueOf", new Class[] { String.class });
            if (valueOf != null) {
                return (T)valueOf.invoke(null, new Object[]{value});
            }
        } catch (Exception ex) {
        }
        try {
            return (T) JSONObject.parseObject(value, defaultValue.getClass());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public List<Setting> find(String module, String key) {
        return settingRepository.findByModuleAndKey(module, key);
    }

    @Cacheable(value = "getPageSize", keyGenerator = "wiselyKeyGenerator")
    public int getPageSize(long tenantId) {
        Integer pageSize = get(tenantId, "gbl_settable", "common.page.size", Integer.class);
        return pageSize == null ? 30 : pageSize;
    }
}
