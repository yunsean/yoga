package com.yoga.core.exception;

public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 28276149133604363L;
	
	public BusinessException() {
		super();
	}	
	public BusinessException(String message){
		super(message);
	}
}
