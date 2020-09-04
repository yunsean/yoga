package com.yoga.core.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
/**
 * 请求参数封装
 * @author Skysea
 *
 */
public class RequestParam{
	private String _appKey;
	private String _sign;
	private long _timestamp;
	private Map<String, String> params;
	
	public RequestParam(){
		this._timestamp = System.currentTimeMillis();
		this.params = new HashMap<>();
	}
	
	public RequestParam(String appKey){
		this._timestamp = System.currentTimeMillis();
		this.params = new HashMap<>();
		this._appKey = appKey;
	}
	
	private static final String DEFAULT_APPKEY_NAME = "token";
	public static RequestParam createByRequest(HttpServletRequest request){
		RequestParam rp = new RequestParam();
		rp.set_appKey(request.getHeader(DEFAULT_APPKEY_NAME));
		rp.set_sign(request.getParameter("_sign"));
		rp.set_timestamp(Long.parseLong(request.getParameter("_timestamp")));
		
    	Map<String, String[]> requestParams = request.getParameterMap();
    	for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
    		String name = iter.next();
    		String[] values = requestParams.get(name);
    		String valueStr = "";
    		for (int i = 0; i < values.length; i++) {
    			valueStr = (i == values.length - 1) ? valueStr + values[i]
    					: valueStr + values[i] + ",";
    		}
    		rp.getParams().put(name, valueStr);
    	}
    	return rp;
	}
	
	public Map<String, String> getMapForSign(){
		Map<String, String> mapForSign = new HashMap<>(params);
		mapForSign.put("_timestamp", ""+_timestamp);
		mapForSign.put("_appKey", _appKey);
		return mapForSign;
	}
	
	private static final long REQUEST_EXPIRE_SECONDS = 20;
	public boolean isExpiredRequest(){
		return System.currentTimeMillis() - _timestamp > REQUEST_EXPIRE_SECONDS *1000;
	}
	public String get_appKey() {
		return _appKey;
	}
	public void set_appKey(String _appKey) {
		this._appKey = _appKey;
	}
	public String get_sign() {
		return _sign;
	}
	public void set_sign(String _sign) {
		this._sign = _sign;
	}
	public long get_timestamp() {
		return _timestamp;
	}
	public void set_timestamp(long _timestamp) {
		this._timestamp = _timestamp;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
