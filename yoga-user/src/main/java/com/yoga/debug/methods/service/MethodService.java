package com.yoga.debug.methods.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonPage;
import com.yoga.debug.methods.model.Group;
import com.yoga.debug.methods.model.Parameter;
import com.yoga.core.utils.StrUtil;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class MethodService {
    private List<com.yoga.debug.methods.model.Group> allGroups = null;
    private Map<String, com.yoga.debug.methods.model.Method> allMethods = null;

    public List<com.yoga.debug.methods.model.Group> getAllMethods() {
        return allGroups;
    }
    public List<com.yoga.debug.methods.model.Group> getAllMethods(String filter) {
        if (allGroups == null) return null;
        if (StrUtil.isBlank(filter)) return allGroups;
        List<com.yoga.debug.methods.model.Group> groups = new ArrayList<>();
        for (Group group : allGroups) {
            Group newGroup = new Group(group.getName());
            for (com.yoga.debug.methods.model.Method method : group.getMethods()) {
                if (method.getExplain() != null && method.getExplain().contains(filter)) {
                    newGroup.addMethod(method);
                }
            }
            if (newGroup.getMethods().size() > 0) {
                groups.add(newGroup);
            }
        }
        return groups;
    }
    public com.yoga.debug.methods.model.Method getMethod(String url) {
        if (allMethods == null) return null;
        return allMethods.get(url);
    }

    public void refresh() throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
        scan.addIncludeFilter(new AssignableTypeFilter(BaseApiController.class));
        Set<BeanDefinition> beanDefinitionSet = scan.findCandidateComponents("com.yoga.**");
        allGroups = new ArrayList<>();
        allMethods = new HashMap<>();
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            Class<?> entityClass = ClassUtils.getClass(beanDefinition.getBeanClassName());
            Group group = new Group(entityClass.getSimpleName());
            String baseModule = "";
            if (entityClass.isAnnotationPresent(Explain.class)) {
                Explain explain = entityClass.getAnnotation(Explain.class);
                if (explain.exclude()) continue;
                group.setName(explain.value());
                group.setModule(explain.module());
                baseModule = explain.module();
            }
            String baseUrl = "";
            if (entityClass.getAnnotation(RequestMapping.class) != null) {
                baseUrl += entityClass.getAnnotation(RequestMapping.class).value()[0];
            }
            Method[] methods = entityClass.getMethods();
            for (Method method : methods) {
                String url = baseUrl;
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    if (method.getAnnotation(RequestMapping.class).value().length > 0) {
                        url += method.getAnnotation(RequestMapping.class).value()[0];
                    }
                    String message = "";
                    String module = "";
                    if (method.isAnnotationPresent(Explain.class)) {
                        Explain explain = method.getAnnotation(Explain.class);
                        if (explain.exclude()) continue;
                        message = explain.value();
                        module = explain.module();
                    }
                    if (StrUtil.isEmpty(module)) module = baseModule;
                    if (StrUtil.isBlank(message)) message = method.getName();
                    com.yoga.debug.methods.model.Method method1 = new com.yoga.debug.methods.model.Method(url, message, module);
                    List<Parameter> parameters1 = new ArrayList<>();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    java.lang.reflect.Parameter[] parameters = method.getParameters();
                    String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                    for (int i = 0; i < parameters.length; i++) {
                        Class<?> parameterType = parameterTypes[i];
                        if (ServletRequest.class.isAssignableFrom(parameterType)) continue;
                        if (ServletResponse.class.isAssignableFrom(parameterType)) continue;
                        if (Model.class.isAssignableFrom(parameterType)) continue;
                        if (BindingResult.class.isAssignableFrom(parameterType)) continue;
                        if ("LoginUser".equals(parameterType.getSimpleName())) continue;
                        boolean requestBody = false;
                        if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                            requestBody = true;
                        }
                        if (CommonPage.class.isAssignableFrom(parameterType)) {
                            parameters1.add(new Parameter("pageIndex", Integer.class.getSimpleName(), "分页页码", requestBody));
                            parameters1.add(new Parameter("pageSize", Integer.class.getSimpleName(), "分页大小", requestBody));
                        } else if (requestBody || parameterType.getClassLoader() == null) {
                            message = "";
                            if (parameters[i].isAnnotationPresent(Explain.class)) {
                                Explain explain = parameters[i].getAnnotation(Explain.class);
                                if (explain.exclude()) continue;
                                message = explain.value();
                            }
                            if (requestBody) {
                                String demo = "";
                                try {
                                    Object object = prototype(parameterType);
                                    if (object != null) {
                                        demo = JSON.toJSONString(object,
                                                SerializerFeature.WriteMapNullValue,
                                                SerializerFeature.WriteNullListAsEmpty,
                                                SerializerFeature.WriteNullNumberAsZero,
                                                SerializerFeature.WriteNullStringAsEmpty,
                                                SerializerFeature.WriteNullBooleanAsFalse,
                                                SerializerFeature.PrettyFormat);
                                    }
                                } catch (Exception ex) {
                                }
                                parameters1.add(new Parameter(parameterNames[i], demo, message, requestBody));
                            } else {
                                parameters1.add(new Parameter(parameterNames[i], parameterType.getSimpleName(), message, requestBody));
                            }
                        } else {
                            Set<String> fieldNames = new HashSet<>();
                            for(; parameterType != Object.class ; parameterType = parameterType.getSuperclass()) {
                                Field[] fields = parameterType.getDeclaredFields();
                                if (fields != null) {
                                    for (Field field : fields) {
                                        String name = field.getName();
                                        String type = field.getType().getSimpleName();
                                        String[] values = null;
                                        String hint = null;
                                        message = "";
                                        if (fieldNames.contains(name)) continue;
                                        fieldNames.add(name);
                                        if (field.isAnnotationPresent(Explain.class)) {
                                            Explain explain = field.getAnnotation(Explain.class);
                                            if (explain.exclude())continue;
                                            message = explain.value();
                                        }
                                        if (field.getType().isEnum()) {
                                            Object[] enums = field.getType().getEnumConstants();
                                            values = new String[enums.length];
                                            for (int index = 0; index < values.length; index++) {
                                                values[index] = enums[index].toString();
                                            }
                                        }
                                        Annotation[] annotations = field.getAnnotations();
                                        List<String> constraints = new ArrayList<>();
                                        for (Annotation annotation : annotations) {
                                            String packageName = annotation.annotationType().getPackage().getName();
                                            if (packageName != null &&
                                                    (packageName.startsWith("javax.validation.constraints") ||
                                                            packageName.startsWith("org.hibernate.validator.constraints"))) {
                                                constraints.add(getAnnotation(annotation));
                                            }
                                            hint = getHint(annotation);
                                        }
                                        parameters1.add(new Parameter(name, type, message, requestBody, constraints, values, hint));
                                    }
                                }
                            }
                        }
                    }
                    method1.setParameters(parameters1);
                    group.addMethod(method1);
                    allMethods.put(method1.getUrl(), method1);
                }
            }
            allGroups.add(group);
        }
    }
    private Object prototype(Class<?> parameterType) {
        try {
            Method[] methods1 = parameterType.getMethods();
            for (Method m : methods1) {
                if ("prototype".equals(m.getName())) {
                    Object object = m.invoke(null);
                    if (object != null) return object;
                }
            }
            Object object = parameterType.newInstance();
            fillObjectWithDefault(object);
            return object;
        } catch (Exception ex) {
            return null;
        }
    }

    private void fillObjectWithDefault(Object object) {
        Class<?> targetClass = object.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.getType().getClassLoader() != null) {
                    Object value = field.getType().newInstance();
                    fillObjectWithDefault(value);
                    field.set(object, value);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getAnnotation(Annotation annotation) {
        Method[] methods = annotation.annotationType().getMethods();
        String message= "";
        String condition = "";
        for (Method method : methods) {
            try {
                String name = method.getName();
                if ("groups".equals(name)) continue;
                if ("payload".equals(name)) continue;
                if ("toString".equals(name)) continue;
                if ("hashCode".equals(name)) continue;
                if ("annotationType".equals(name)) continue;
                if ("message".equals(name)) message = method.invoke(annotation).toString();
                else condition += method.getName() + "=" +  method.invoke(annotation).toString() + ", ";
            } catch (Exception ex) {
            }
        }
        if (condition.length() > 2) condition = condition.substring(0, condition.length() - 2);
        if (condition.length() > 0) condition += ", ";
        return annotation.annotationType().getSimpleName() + "(" + condition + "message=" + message + ")";
    }

    private String getHint(Annotation annotation) {
        Method[] methods = annotation.annotationType().getMethods();
        String message= "";
        String condition = "";
        for (Method method : methods) {
            try {
                String name = method.getName();
                if ("pattern".equals(name)) {
                    return method.invoke(annotation).toString();
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }
}
