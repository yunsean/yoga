package com.yoga.core.utils;

import com.yoga.core.annotation.FreemarkerFormat;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class FreemarkerUtil {

    public static String getTemplate(String template, Map map) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("contract", template);
            cfg.setTemplateLoader(stringLoader);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            Template temp = cfg.getTemplate("contract", "utf-8");
            StringWriter stringWriter = new StringWriter();
            temp.process(map, stringWriter);
            return stringWriter.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static void buildTemplate(PrintWriter writer, String template, Map map) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("contract", template);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateLoader(stringLoader);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            Template temp = cfg.getTemplate("contract", "utf-8");
            temp.process(map, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Map<String, String> filedsForType(String variableName, Class<?> parameterType) {
        if (StringUtil.isNotBlank(variableName)) variableName = variableName + ".";
        Map<String, String> allFields = new LinkedHashMap<>();
        for (; parameterType != Object.class; parameterType = parameterType.getSuperclass()) {
            Field[] fields = parameterType.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    String name = field.getName();
                    if (allFields.containsKey(name)) continue;
                    if (field.isAnnotationPresent(FreemarkerFormat.class)) {
                        FreemarkerFormat explain = field.getAnnotation(FreemarkerFormat.class);
                        String meaning = explain.value();
                        String format = explain.format();
                        if (StringUtil.isNotBlank(explain.pattern()))
                            allFields.put(meaning, explain.pattern().replace("*.", variableName));
                        else allFields.put(meaning, "${(" + variableName + name + format + ")!}");
                    }
                }
                Method[] methods = parameterType.getMethods();
                for (Method method : methods) {
                    String name = method.getName();
                    if (allFields.containsKey(name)) continue;
                    if (method.isAnnotationPresent(FreemarkerFormat.class)) {
                        FreemarkerFormat explain = method.getAnnotation(FreemarkerFormat.class);
                        String meaning = explain.value();
                        String format = explain.format();
                        if (StringUtil.isNotBlank(explain.pattern()))
                            allFields.put(meaning, explain.pattern().replace("*.", variableName));
                        else allFields.put(meaning, "${(" + variableName + name + "()" + format + ")!}");
                    }
                }
            }
        }
        return allFields;
    }
}
