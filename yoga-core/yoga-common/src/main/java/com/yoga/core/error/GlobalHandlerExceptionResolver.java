package com.yoga.core.error;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.UndeclaredThrowableException;

public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) {
        exception.printStackTrace();
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception.getClass().getSimpleName());
        String message = exception.getMessage();
        if (exception instanceof UndeclaredThrowableException) {
            message = ((UndeclaredThrowableException) exception).getUndeclaredThrowable().getMessage();
        }
        mav.addObject("message", message);
        mav.addObject("object", object);
        mav.addObject("path", request.getRequestURI());
        mav.setViewName("/error/error");
        return mav;
    }
}
