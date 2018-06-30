package com.yoga.core.controller;

import com.yoga.core.exception.BusinessException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public abstract class BaseWebController extends BaseController{

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	String handleAllException(MissingServletRequestParameterException ex, HttpServletResponse response, ModelMap model) throws IOException {
		return "/error/error";
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(IllegalArgumentException.class)
	String handleAllException(IllegalArgumentException ex, HttpServletResponse response, ModelMap model) throws IOException {
		model.put("error", ex.getMessage());
		return "/error/error";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(BusinessException.class)
	String handleAllException(BusinessException ex, HttpServletResponse response, ModelMap model) throws IOException {
		model.put("error", ex.getMessage());
		return "/error/error";
	}
	
	@ResponseStatus(HttpStatus.FOUND)
	@ExceptionHandler(UnauthenticatedException.class)
	String handleAllException(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return "redirect:/admin/toLogin";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnauthorizedException.class)
	String handleAllException(UnauthorizedException ex, HttpServletResponse response) throws IOException {
		return "/error/permission";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	String handleAllException(Exception ex, HttpServletResponse response, ModelMap model) throws IOException {
		model.put("error", ex.getMessage());
		return "/error/error";
	}
}

