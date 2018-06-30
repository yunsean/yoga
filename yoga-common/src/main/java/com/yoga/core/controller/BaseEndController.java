package com.yoga.core.controller;

import com.yoga.core.exception.BusinessException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;


public abstract class BaseEndController extends BaseController{

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ModelAndView handleAllException(MissingServletRequestParameterException ex, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView("/error/error");
		mav.addObject("error", ex.getMessage());
		return mav;
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(IllegalArgumentException.class)
	protected ModelAndView handleAllException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView("/error/error");
		mav.addObject("error", ex.getMessage());
		return mav;
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(BusinessException.class)
	protected ModelAndView handleAllException(BusinessException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mav = customizeHandle(ex, request, response);
		if (mav != null) return mav;
		mav = new ModelAndView("/error/error");
		mav.addObject("error", ex.getMessage());
		return mav;
	}

	@ResponseStatus(HttpStatus.FOUND)
	@ExceptionHandler(UnauthenticatedException.class)
	protected ModelAndView handleAllException(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String view;
		if (request.getRequestURI() != null) {
			String query = (request.getQueryString() == null) ? "" : ("?" + request.getQueryString());
			view = "redirect:/web/toLogin?uri=" + request.getRequestURI() + query;
		} else {
			view = "redirect:/web/toLogin";
		}
		ModelAndView mav = new ModelAndView(view);
		return mav;        
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnauthorizedException.class)
	protected ModelAndView handleAllException(UnauthorizedException ex, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView("/error/error");
		mav.addObject("error", ex.getMessage());
		return mav;
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	protected ModelAndView handleAllException(Exception ex, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView("/error/error");
		mav.addObject("error", ex.getMessage());
		return mav;
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UndeclaredThrowableException.class)
	protected ModelAndView handleAllException(UndeclaredThrowableException ex, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView("/error/error");
		mav.addObject("error", ex.getMessage());
		return mav;
	}

	protected ModelAndView customizeHandle(BusinessException ex, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
}

