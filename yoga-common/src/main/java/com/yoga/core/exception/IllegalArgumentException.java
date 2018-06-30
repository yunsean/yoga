package com.yoga.core.exception;

import org.springframework.validation.BindingResult;

public class IllegalArgumentException extends java.lang.IllegalArgumentException {
    public IllegalArgumentException(BindingResult bindingResult) {
        super(bindingResult.getFieldError().getDefaultMessage());
    }
    public IllegalArgumentException(String message) {
        super(message);
    }
}
