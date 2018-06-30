package com.yoga.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yoga.core.data.CommonMessage;

public abstract class BaseController {
	protected Logger logger = Logger.getLogger(getClass());
	
	protected String getMessage(Exception e){
		return null == e.getMessage() ? CommonMessage.EXCEPTION_MESSAGE : e.getMessage();
	}
	
	protected String getRequestHost(HttpServletRequest request){
		return request.getRemoteHost();
	}
}
