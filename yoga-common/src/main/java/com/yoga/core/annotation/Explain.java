package com.yoga.core.annotation;


import java.lang.annotation.*;
import java.lang.reflect.Method;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Explain {
    String value() default "";
    String module() default "";
    boolean exclude() default false;
}
