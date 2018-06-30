package com.yoga.tenant.setting;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Settables {
    Settable[] value();
}