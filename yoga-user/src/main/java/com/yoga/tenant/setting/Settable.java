package com.yoga.tenant.setting;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Repeatable(Settables.class)
public @interface Settable {
    String key() default "";
    String name() default "";
    String module() default "";
    String[] showWith() default "";       //当指定的模块存在（任意一个）时，该配置项才显示，不配置则不筛选
    Class type() default String.class;    //BaseEnum or SettableDataSource
    boolean systemOnly() default false;
    boolean showPage() default false;     //点击配置按钮是否在新的页面（而不是弹出框）中打开
    String defaultValue() default "";
    String url() default "";
}