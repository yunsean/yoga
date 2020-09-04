package com.yoga.utility.qr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class QRBindInfo implements Serializable {
	private Map<String, String> headers;
	private Map<String, Object> params;
	private String url;
	private String token;

	public QRBindInfo(HttpServletRequest request, String url, Map<String, Object> params, String token) {
		this.params = params;
		this.url = url;
		this.token = token;
		this.headers = new HashMap<>();
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getHeader(name);
			if (value != null) {
				headers.put(name, value);
			}
		}
		if (request.getParameterMap() != null) {
			Map<String, String[]> parameters = request.getParameterMap();
			parameters.keySet().forEach(key-> {
				String[] value = parameters.get(key);
				if (value != null && value.length > 0) headers.put(key, value[0]);
			});
		}
	}
}
