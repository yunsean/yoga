package com.yoga.core.data;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL) 
public class CommonResult implements Serializable {
	private static final long serialVersionUID = 2120869894112984147L;
	private Integer code = 0;
	private String message = null;
	private Object result = null;
	private CommonPage page = null;

	public CommonResult() {}

	public CommonResult(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public CommonResult(Object result) {
		this.result = result;
	}
	public CommonResult(Object result, CommonPage page) {
		this.result = result;
		this.page = page;
	}
	public CommonResult(Object result, Page<?> page) {
		this.result = result;
		this.page = new CommonPage(page);
	}
	public CommonResult(int code, String message, Object result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}
	public CommonResult(int code, String message, Object result, CommonPage page) {
		this.code = code;
		this.message = message;
		this.page = page;
		this.result = result;
	}
	public CommonResult(String message, Object result) {
		this.message = message;
		this.result = result;
	}
	public CommonResult(String message, Object result, CommonPage page) {
		this.message = message;
		this.page = page;
		this.result = result;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public CommonPage getPage() {
		return page;
	}
	public void setPage(CommonPage page) {
		this.page = page;
	}

	public String toFtl(ModelMap model, String url){
		return url;
	}
	public String toFtlForError(ModelMap model){
		return "error";
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}

