package com.yoga.logging.aspect;

import com.yoga.core.base.BaseEnum;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.service.LoggingService;
import com.yoga.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
@Order(1)
public class LoggingAspect {
    protected Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private LoggingService loggingService;

    @Pointcut("@annotation(com.yoga.logging.annotation.Logging)")
    public void aspect() {
    }

    @AfterReturning(value = "aspect()", returning = "result")
    public void doAfter(JoinPoint joinPoint, Object result) {
        Logging logging = getLogging(joinPoint);
        if (logging == null) return;
        try {
            String method = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();
            Object primaryId = null;
            Set<Integer> excludeArgs = null;
            if (logging.excludeArgs().length > 1 || logging.excludeArgs()[0] != -2) {
                excludeArgs = new HashSet<>();
                for (int index : logging.excludeArgs()) {
                    excludeArgs.add(index);
                }
            }
            if (logging.primaryKeyIndex() >= 0 && args.length > logging.primaryKeyIndex()) {
                primaryId = args[logging.primaryKeyIndex()];
            } else if (logging.primaryKeyIndex() == -1) {
                primaryId = result;
            }
            Long tenantId = null;
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            try { tenantId = Long.valueOf(request.getParameter("tid")); } catch (Exception ex) { }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(method).append("(");
            for (int i = 0; i < args.length; i++) {
                if (i > 0) stringBuilder.append(", ");
                if (excludeArgs != null && excludeArgs.contains(i)) stringBuilder.append("");
                else stringBuilder.append(getValue(args[i]));
            }
            stringBuilder.append(")");
            method = stringBuilder.toString();
            String argNames = logging.argNames();
            stringBuilder = new StringBuilder();
            stringBuilder.append(logging.description()).append("：");
            if (StringUtil.isNotBlank(argNames)) {
                String[] argNameArray = argNames.replaceAll("，", ",").split(",");
                for (int i = 0; i < argNameArray.length && i < args.length; i++) {
                    if (excludeArgs != null && excludeArgs.contains(i))continue;
                    if (StringUtil.isNotBlank(argNameArray[i])) {
                        stringBuilder.append("\n").append(argNameArray[i]).append("=").append(getValue(args[i]));
                    }
                }
            }
            loggingService.add(tenantId, logging.module(), method, primaryId, null, logging.description(), stringBuilder.toString(), result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getValue(Object object) {
        if (object == null) return "<null>";
        else if (object instanceof BaseEnum) return ((BaseEnum) object).getName();
        else return object.toString();
    }
    private static Logging getLogging(JoinPoint joinPoint) {
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        return method.getAnnotation(Logging.class);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
