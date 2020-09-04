package com.yoga.admin.aggregate.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.spring.SpringContext;
import com.yoga.admin.aggregate.ao.Schedule;
import com.yoga.admin.aggregate.ao.Statement;
import com.yoga.admin.aggregate.ao.Todo;
import org.apache.commons.lang3.ClassUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AggregateService extends BaseService {

    @Autowired
    private SpringContext springContext;
    private List<AggregateActor> aggregateActors = new ArrayList<>();

    @PostConstruct
    public void loadPushService() {
        logger.info("查找信息聚合服务");
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(AggregateActor.class));
        Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("com.yoga.**");
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            try {
                Class<?> entityClass = ClassUtils.getClass(beanDefinition.getBeanClassName());
                AggregateActor actor = (AggregateActor) springContext.getApplicationContext().getBean(entityClass);
                aggregateActors.add(actor);
                logger.info("找到信息聚合服务：" + entityClass.getSimpleName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.info("共找到" + aggregateActors.size() + "个信息聚合服务");
    }

    @Cacheable(value = "aggregate", keyGenerator = "wiselyKeyGenerator")
    public List<Schedule> getSchedules(long tenantId, long userId) {
        Set<String> permissions = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute("permissions");
        if (permissions == null) permissions = new HashSet<>();
        Set<String> modules = permissions.stream().map(permission-> {
            int index = permission.indexOf('.');
            if (index > 0) return permission.substring(0, index);
            else return permission;
        }).collect(Collectors.toSet());
        List<Schedule> schedules = new ArrayList<>();
        aggregateActors.forEach(actor-> {
            List<Schedule> adds = actor.getSchedules(tenantId, userId, modules);
            if (adds != null) schedules.addAll(adds);
        });
        schedules.sort(Comparator.comparingInt(Schedule::getSort));
        return schedules;
    }

    @Cacheable(value = "aggregate", keyGenerator = "wiselyKeyGenerator")
    public List<Statement> getStatements(long tenantId, long userId) {
        Set<String> permissions = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute("permissions");
        if (permissions == null) permissions = new HashSet<>();
        Set<String> modules = permissions.stream().map(permission-> {
            int index = permission.indexOf('.');
            if (index > 0) return permission.substring(0, index);
            else return permission;
        }).collect(Collectors.toSet());
        List<Statement> statements = new ArrayList<>();
        aggregateActors.forEach(actor-> {
            List<Statement> adds = actor.getStatements(tenantId, userId, modules);
            if (adds != null) statements.addAll(adds);
        });
        return statements;
    }
    public List<Todo> getTodos(long tenantId, long userId) {
        Set<String> permissions = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute("permissions");
        if (permissions == null) permissions = new HashSet<>();
        Set<String> modules = permissions.stream().map(permission-> {
            int index = permission.indexOf('.');
            if (index > 0) return permission.substring(0, index);
            else return permission;
        }).collect(Collectors.toSet());
        List<Todo> todos = new ArrayList<>();
        aggregateActors.forEach(actor-> {
            List<Todo> adds = actor.getTodos(tenantId, userId, modules);
            if (adds != null) todos.addAll(adds);
        });
        return todos;
    }
}
