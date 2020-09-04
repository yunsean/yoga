package com.yoga.core.data.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestFormat {
    String JSON = "json";
    String XML = "xml";
    String RAW = "raw";
    String value() default "";
}