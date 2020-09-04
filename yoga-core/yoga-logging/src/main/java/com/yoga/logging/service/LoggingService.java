package com.yoga.logging.service;

import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.cache.PrimaryInfoCache;
import com.yoga.logging.mapper.LoggingMapper;
import com.yoga.logging.model.Logging;
import com.yoga.logging.model.LoginUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.DateUtil;
import com.yoga.core.utils.StringUtil;
import org.apache.commons.lang3.ClassUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class LoggingService {

    @Autowired
    private LoggingMapper loggingMapper;
    @Autowired
    private PrimaryInfoCache primaryInfoCache;

    private static LoggingService instance;
    @PostConstruct
    private void postConstruct() {
        instance = this;
    }

    public static void add(long tenantId, String module, String description, Object primaryId) {
        add(tenantId, module, description, primaryId, null, null, null);
    }
    public static void add(long tenantId, String module, String description, Object primaryId, String detail) {
        add(tenantId, module, description, primaryId, null, detail, null);
    }
    public static void add(long tenantId, String module, String description, Object primaryId, String primaryInfo, String detail, Object result) {
        String method;
        try {
            StackTraceElement[] stackTraces = new Throwable().getStackTrace();
            if (stackTraces != null && stackTraces.length > 1) {
                method = stackTraces[1].getClassName() + ":" + stackTraces[1].getMethodName();
            } else {
                method = "Unknown";
            }
        } catch (Exception ex) {
            method = "Unknown";
        }
        instance.add(tenantId, module, method, primaryId, primaryInfo, description, detail, result);
    }

    Map<String, LoggingPrimaryHandler> allPrimaryHandlers = new HashMap<>();
    Map<String, String> allModuleNames = null;

    public Map<String, String> allModules() {
        if (allModuleNames == null) {
            synchronized (LoggingService.class) {
                if (allModuleNames == null) {
                    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
                    provider.addIncludeFilter(new AnnotationTypeFilter(LoggingPrimary.class));
                    allModuleNames = new HashMap<>();
                    Set<BeanDefinition> definitions = provider.findCandidateComponents("com.yoga.**");
                    for (BeanDefinition definition : definitions) {
                        try {
                            Class<?> entityClass = ClassUtils.getClass(definition.getBeanClassName());
                            LoggingPrimary loggingPrimary = entityClass.getAnnotation(LoggingPrimary.class);
                            if (loggingPrimary != null) allModuleNames.put(loggingPrimary.module(), loggingPrimary.name());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return allModuleNames;
    }

    public String getInfo(String module, Object primaryId) {
        LoggingPrimaryHandler handler = allPrimaryHandlers.get(module);
        if (handler == null) return null;
        return handler.getPrimaryInfo(primaryId);
    }

    public void add(long tenantId, String module, String method, Object primaryId, String primaryInfo, String description, String detail, Object result) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getSession().getAttribute("user");
        add(tenantId, loginUser.getId(), loginUser.getName(), module, module, primaryId, primaryInfo, description, detail, result);
    }
    public void add(long tenantId, long userId, String username, String module, String method, Object primaryId, String primaryInfo, String description, String detail, Object result) {
        try {
            String primaryKey = null;
            if (primaryId != null && primaryInfo == null) {
                primaryKey = primaryId.toString();
                primaryInfo = primaryInfoCache.primaryInfo(module, primaryId);
            }
            String resultInfo = null;
            if (result != null) resultInfo = result.toString();
            Logging logging = new Logging(tenantId, userId, username, module, method, primaryKey, primaryInfo, description, detail, resultInfo);
            loggingMapper.insert(logging);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PageInfo<Logging> list(long tenantId, Long userId, String module, String filter, String primaryId, Date beginDate, Date endDate, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Logging> loggings = new MapperQuery<>(Logging.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("module", module, StringUtil.isNotBlank(module))
                .andEqualTo("userId", userId, userId != null)
                .andEqualTo("primaryId", primaryId, primaryId != null)
                .andGreaterThanOrEqualTo("createTime", DateUtil.beginOfDay(beginDate), beginDate != null)
                .andLessThanOrEqualTo("createTime", DateUtil.endOfDay(endDate), endDate != null)
                .and()
                .orLike("method", "%" + filter + "%", StringUtil.isNotBlank(filter))
                .orLike("primaryInfo", "%" + filter + "%", StringUtil.isNotBlank(filter))
                .orLike("description", "%" + filter + "%", StringUtil.isNotBlank(filter))
                .orLike("detail", "%" + filter + "%", StringUtil.isNotBlank(filter))
                .orderBy("createTime", true)
                .query(loggingMapper);
        return new PageInfo<>(loggings);
    }

    public Logging get(long id) {
        Logging logging = loggingMapper.selectByPrimaryKey(id);
        if (logging == null) throw new BusinessException("指定日志不存在！");
        return logging;
    }

    public String getModuleName(String module) {
        if (module == null) return "";
        String name = allModuleNames.get(module);
        if (name == null) return module;
        else return name;
    }
}
