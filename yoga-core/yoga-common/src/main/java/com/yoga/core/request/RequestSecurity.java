package com.eshop.core.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.yoga.core.request.RequestParam;
import com.yoga.core.utils.AesUtil;
import com.yoga.core.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;
import org.h2.security.AES;

/**
 * 请求参数校验
 * @author Skysea
 *
 */
public class RequestSecurity {

	private static final Gson GSON = new Gson();
	private static final String KEY = "123456";
	private static final String IV = "123456";

	public static String verifyRequest(HttpServletRequest request){
		RequestParam rp = RequestParam.createByRequest(request);
		String baseValidate = validateParam(rp);
		if(null != baseValidate){
			return baseValidate;
		}
		if(!isLegalSign(rp)){
			return "签名不合法";
		}
		return null;
	}

	private static String validateParam(RequestParam rp){
		if(null == rp){
			return "请求不合法";
		}
		if(rp.isExpiredRequest()){
			return "请求过期";
		}
		if(StringUtil.isBlank(rp.get_sign())){
			return "签名不能为空";
		}
		return null;
	}

	public static String signRequest(RequestParam rp){
		return sign(rp.getMapForSign());
	}

	private static boolean isLegalSign(RequestParam rp){
		String sign = sign(rp.getMapForSign());
		if(StringUtil.isBlank(sign)){
			return false;
		}
		if(!sign.equals(rp.get_sign())){
			return false;
		}
		return true;
	}

	private static String sign(Map<String, String> toSign){
		String content = GSON.toJson(sortMap(toSign));
		String sign = null;
		try {
			sign = DigestUtils.md5Hex(AesUtil.encrypt(content, KEY, IV));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	private static List<Map.Entry<String, String>> sortMap(final Map<String, String> map) {
		final List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());

		// 重写集合的排序方法：按字母顺序
		Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
			@Override
			public int compare(final Entry<String, String> o1, final Entry<String, String> o2) {
				return (o1.getKey().toString().compareTo(o2.getKey()));
			}
		});

		return infos;
	}

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		map.put("dafsdfas", "12312");
		map.put("dafs", "12312");
		RequestParam param = new RequestParam();
		param.setParams(map);
		param.set_appKey("111111");
		param.set_sign(signRequest(param));

		System.out.println(isLegalSign(param));
	}
}
