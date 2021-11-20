package com.yoga.core.base;

import com.alibaba.fastjson.JSON;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.redis.lock.RedisLocker;
import com.yoga.core.redis.lock.UnableToAquireLockException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class BaseController {

	@Autowired
	protected RedisLocker redisLocker;
	protected interface LockRunnable<T> {
		T run();
	}

	protected String getMessage(Exception e){
		return null == e.getLocalizedMessage() ? EXCEPTION_MESSAGE : e.getLocalizedMessage();
	}

	protected String getRequestHost(HttpServletRequest request){
		return request.getRemoteHost();
	}

	public static final String EXCEPTION_MESSAGE = "服务器出了点小问题,请稍后重试";

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ModelAndView handleAllException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		if (isApi(request)) return responseResult(response, new CommonResult(ResultConstants.ERROR_MISSINGPARAM, getMessage(ex)));
		else return responseView404(ex.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(IllegalArgumentException.class)
	protected ModelAndView handleAllException(IllegalArgumentException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		if (isApi(request)) return responseResult(response, new CommonResult(ResultConstants.ERROR_ILLEGALPARAM, getMessage(ex)));
		else return responseView404(ex.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(BusinessException.class)
	protected ModelAndView handleAllException(BusinessException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		if (isApi(request)) return responseResult(response, new CommonResult(ex.getErrorCode(), getMessage(ex), ex.getResult()));
		else return responseView404(ex.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnauthenticatedException.class)
	protected String handleAllException(UnauthenticatedException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		if (isApi(request)) responseResult(response, new CommonResult(ResultConstants.ERROR_UNAUTHENTICAED, "请登录"));
		else redirect(response, "/admin/toLogin");
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnauthorizedException.class)
	protected ModelAndView handleAllException(UnauthorizedException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		if (isApi(request)) return responseResult(response, new CommonResult(ResultConstants.ERROR_UNAUTHORIZED, "未授权"));
		else return responseView404(ex.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnableToAquireLockException.class)
	protected ModelAndView handleAllException(UnableToAquireLockException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		if (isApi(request)) return responseResult(response, new CommonResult(ResultConstants.ERROR_UNABLETOAQUIRELOCK, "服务器忙，请稍后重试"));
		else return responseView404(ex.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	protected ModelAndView handleAllException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ex.printStackTrace();
		if (isApi(request)) return responseResult(response, new CommonResult(ResultConstants.ERROR_UNKNOWN, getMessage(ex)));
		else return responseView404(ex.getLocalizedMessage());
	}

	public static void redirect(HttpServletResponse response, String url) throws IOException {
		response.setStatus(HttpStatus.FOUND.value());
		response.sendRedirect(url);
	}
	private static ModelAndView responseResult(HttpServletResponse response, CommonResult result) {
		try {
			String json = JSON.toJSONString(result);
			response.setHeader("Content-Type", "application/json; charset=utf-8");
			response.getWriter().write(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	private static ModelAndView responseView404(String message) {
		ModelAndView model = new ModelAndView();
		model.setViewName("/error/404");
		model.addObject("message", message);
		return model;
	}
	public static Boolean isApi(HttpServletRequest request) {
		String uri = request.getRequestURI().toLowerCase();
		if (uri.startsWith("/api/") || uri.endsWith(".json")) return true;
		else return false;
	}

	protected <T> T runInLock(String key, LockRunnable<T> runnable) {
		try {
			return redisLocker.lock(key, ()-> {
				return runnable.run();
			});
		} catch (BusinessException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ex.getMessage());
		}
	}
	protected void runInLock(String key, Runnable runnable) {
		try {
			redisLocker.lock(key, ()-> {
				runnable.run();
				return true;
			});
		} catch (BusinessException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ex.getMessage());
		}
	}
}
