package com.yoga.core.annotation;


import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FreemarkerFormat {
    String value() default "";
    String format() default "";
    String pattern() default "";
    boolean exclude() default false;
}
