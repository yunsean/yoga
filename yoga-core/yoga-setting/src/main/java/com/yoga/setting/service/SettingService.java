package com.yoga.setting.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yoga.core.base.BaseService;
import com.yoga.core.data.CommonPage;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.model.Logging;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.logging.service.LoggingService;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.annotation.Settables;
import com.yoga.setting.cache.SettingCache;
import com.yoga.setting.mapper.SettingMapper;
import com.yoga.setting.model.SaveItem;
import com.yoga.setting.model.SettableItem;
import com.yoga.setting.model.Setting;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class SettingService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private SettingMapper settingMapper;
    @Autowired
    private SettingCache settingCache;
    @Autowired
    private Gson gson;

    public final static String Permission_Update = "sys_config.update";
    public final static String ModuleName = "sys_config";
    public final static String Key_PageSize = "common.page.size";
    public static String getLockForKey(long tenantId, String module, String name) {
        return SettingService.class.getName() + "." + tenantId + "." + module + "." + name;
    }

    @Override
    public String getPrimaryInfo(Object primaryId) {
        Setting setting = settingMapper.selectByPrimaryKey(primaryId);
        if (setting == null) return null;
        return setting.getKey();
    }

    private static List<SettableItem> settableItems = new ArrayList<>();

    @PostConstruct
    public void loadSettable() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Settable.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Settables.class));
        logger.info("查找配置项服务");
        settableItems.clear();
        Set<BeanDefinition> definitions = provider.findCandidateComponents("com.yoga");
        for (BeanDefinition definition : definitions) {
            try {
                Class<?> entityClass = ClassUtils.getClass(definition.getBeanClassName());
                if (entityClass.isAnnotationPresent(Settable.class) || entityClass.isAnnotationPresent(Settables.class)) {
                    Settable[] settables = entityClass.getAnnotationsByType(Settable.class);
                    for (Settable settable : settables) {
                        if (StringUtil.isNotBlank(settable.module())) {
                            settableItems.add(new SettableItem(settable, settable.url()));
                            logger.info("找到配置服务：" + settable.name());
                        }
                    }
                }
                String url = "";
                if (entityClass.getAnnotation(RequestMapping.class) != null) {
                    url += entityClass.getAnnotation(RequestMapping.class).value()[0];
                } else if (entityClass.getAnnotation(GetMapping.class) != null) {
                    url += entityClass.getAnnotation(GetMapping.class).value()[0];
                }
                Method[] methods = entityClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Settable.class) || entityClass.isAnnotationPresent(Settables.class)) {
                        String localUrl = url;
                        if (method.getAnnotation(RequestMapping.class) != null) {
                            localUrl += method.getAnnotation(RequestMapping.class).value()[0];
                        } else if (method.getAnnotation(GetMapping.class) != null) {
                            localUrl += method.getAnnotation(GetMapping.class).value()[0];
                        }
                        Settable[] settables = method.getAnnotationsByType(Settable.class);
                        for (Settable settable : settables) {
                            if (StringUtil.isNotBlank(settable.module())) {
                                settableItems.add(new SettableItem(settable, StringUtil.isNotBlank(settable.url()) ? settable.url() : localUrl));
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

    public static PageInfo<SettableItem> allSettable(String filter, String[] modules, int pageIndex, int pageSize, boolean systemOnly) {
        List<SettableItem> items = new ArrayList<>();
        int begin = pageIndex * pageSize;
        int end = begin + pageSize;
        int count = 0;
        if (StringUtil.isBlank(filter)) filter = null;
        for (SettableItem item : settableItems) {
            if (item.isSystemOnly() != systemOnly) continue;
            if (filter != null && !item.matchFilter(filter)) continue;
            if (!item.matchModule(modules)) continue;
            if (count >= begin && count < end) {
                items.add(new SettableItem(item));
            }
            count++;
        }
        PageInfo<SettableItem> result = new PageInfo<>();
        result.setList(items);
        result.setPageSize(pageSize);
        result.setTotal(count);
        result.setPageNum(pageIndex + 1);
        return result;
    }

    public <T> void save(long tenantId, String module, String key, T value, String showValue) {
        runInLock(getLockForKey(tenantId, module, key), ()-> {
            Setting setting = new MapperQuery<>(Setting.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("module", module)
                    .andEqualTo("key", key)
                    .queryFirst(settingMapper);
            String sValue = null;
            if (value != null) {
                Class<?> clazz = value.getClass();
                if (clazz.isPrimitive()) {
                    sValue = value.toString();
                } else if (clazz == String.class) {
                    sValue = value.toString();
                } else {
                    sValue = gson.toJson(value);
                }
            }
            if (setting == null) {
                setting = new Setting(tenantId, module, key, sValue, showValue);
                settingMapper.insert(setting);
            } else {
                setting.setValue(sValue);
                setting.setShowValue(showValue);
                settingMapper.updateByPrimaryKey(setting);
            }
            settingCache.flush(tenantId, module, key);
        });
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
        if (clazz == String.class) {
            return (T)value;
        } else if (clazz == LocalTime.class) {
            return (T) LocalTime.parse(value);
        } else if (clazz == LocalDate.class) {
            return (T) LocalDate.parse(value);
        } else if (clazz == LocalDateTime.class) {
            return (T) LocalDateTime.parse(value);
        }
        try {
            Method valueOf = clazz.getMethod("valueOf", new Class[] { String.class });
            if (valueOf != null) {
                return (T)valueOf.invoke(null, new Object[]{value});
            }
        } catch (Exception ex) {
        }
        try {
            return (T) gson.fromJson(value, clazz);
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
            } else if (defaultValue.getClass() == String.class) {
                return (T) value;
            } else if (defaultValue.getClass() == LocalTime.class) {
                return (T) LocalTime.parse(value);
            } else if (defaultValue.getClass() == LocalDate.class) {
                return (T) LocalDate.parse(value);
            } else if (defaultValue.getClass() == LocalDateTime.class) {
                return (T) LocalDateTime.parse(value);
            }
            Method valueOf = defaultValue.getClass().getMethod("valueOf", new Class[] { String.class });
            if (valueOf != null) {
                return (T)valueOf.invoke(null, new Object[]{value});
            }
        } catch (Exception ex) {
        }
        try {
            return (T) gson.fromJson(value, defaultValue.getClass());
        } catch (Exception ex) {
            return defaultValue;
        }
    }
    public <T> T get(long tenantId, String module, String key, Type clazz) {
        Setting setting = settingCache.get(tenantId, module, key);
        if (setting == null) return null;
        String value = setting.getValue();
        if (value == null) return null;
        try {
            return (T) gson.fromJson(value, clazz);
        } catch (Exception ex) {
            return null;
        }
    }
    public List<Setting> list(String module, String key) {
        return new MapperQuery<>(Setting.class)
                .andEqualTo("module", module)
                .andEqualTo("key", key)
                .query(settingMapper);
    }
    public int getPageSize(long tenantId) {
        Integer pageSize = get(tenantId, ModuleName, Key_PageSize, Integer.class);
        return pageSize == null ? 30 : pageSize;
    }
}
