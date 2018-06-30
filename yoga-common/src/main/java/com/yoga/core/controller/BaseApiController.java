package com.yoga.core.controller;

import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.BusinessException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseApiController extends BaseController{
	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	CommonResult handleAllException(MissingServletRequestParameterException ex, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		return new CommonResult(ResultConstants.ERROR_MISSINGPARAM, getMessage(ex));
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(IllegalArgumentException.class)
	CommonResult handleAllException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		return new CommonResult(ResultConstants.ERROR_ILLEGALPARAM, getMessage(ex));
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(BusinessException.class)
	CommonResult handleAllException(BusinessException ex, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		return new CommonResult(ResultConstants.ERROR_BUSINESSERROR, getMessage(ex));
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnauthenticatedException.class)
	CommonResult handleAllException(UnauthenticatedException ex, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		return new CommonResult(ResultConstants.ERROR_UNAUTHENTICAED, "请登录");
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnauthorizedException.class)
	CommonResult handleAllException(UnauthorizedException ex, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		return new CommonResult(ResultConstants.ERROR_UNAUTHORIZED, "未授权");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	CommonResult handleAllException(Exception ex, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		return new CommonResult(ResultConstants.ERROR_UNKNOWN, getMessage(ex));
	}
}

