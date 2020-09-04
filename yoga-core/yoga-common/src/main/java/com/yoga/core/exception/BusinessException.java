package com.yoga.core.exception;

import com.yoga.core.data.ResultConstants;
import org.aspectj.apache.bcel.ExceptionConstants;

public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 28276149133604363L;
	private int errorCode = ResultConstants.ERROR_BUSINESSERROR;
	private Object result = null;
	
	public BusinessException() {
		super();
	}
	public BusinessException(String message){
		super(message);
	}
	public BusinessException(Throwable throwable){
		super(throwable.getMessage() == null ? "Unknown Exception!" : throwable.getMessage());
	}
	public BusinessException(int errorCode, String message){
		super(message);
		this.errorCode = errorCode;
	}
	public BusinessException(int errorCode, String message, Object result){
		super(message);
		this.errorCode = errorCode;
		this.result = result;
	}

	public int getErrorCode() {
		return errorCode;
	}
	public Object getResult() {
		return result;
	}
}
