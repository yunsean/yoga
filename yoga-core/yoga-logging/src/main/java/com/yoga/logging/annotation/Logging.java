package com.yoga.logging.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {
    String module();
    String description();
    int primaryKeyIndex();              //-1代表返回值，其他负数无效
    String argNames() default "";       //逗号分隔的函数参数备注
    int[] excludeArgs() default -2;
    boolean allowLose() default true;
}
