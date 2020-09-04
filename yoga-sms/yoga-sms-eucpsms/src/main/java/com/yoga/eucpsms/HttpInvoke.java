package com.yoga.eucpsms;

import com.yoga.core.utils.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class HttpInvoke {

	public static String execute(String method, String url, String body) throws Exception {
		return execute(method, url, body, null);
	}
	public static String execute(String method, String url, String body, boolean checkStatusCode) throws Exception {
		return execute(method, url, body, null, checkStatusCode);
	}
	public static String execute(String method, String url, String body, String contentType) throws Exception {
		return execute(method, url, body, contentType, true);
	}
	public static String execute(String method, String url, String body, String contentType, boolean checkStatusCode) throws Exception {
		try {
			URL uri = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setRequestMethod(method); 
			urlConnection.setConnectTimeout(5000); 
			urlConnection.setDoOutput(body != null && body.length() > 0);
			urlConnection.setReadTimeout(5000); 
			urlConnection.setDoInput(true); 
			urlConnection.setUseCaches(false); 
			urlConnection.setInstanceFollowRedirects(true);
			if (StringUtil.isNotBlank(contentType)) urlConnection.setRequestProperty("Content-Type", contentType);
			urlConnection.connect();
			if (body != null && body.length() > 0) {
				OutputStream os = urlConnection.getOutputStream();
				os.write(body.toString().getBytes(Charset.defaultCharset()));
				os.close();
			}
			int code = urlConnection.getResponseCode();
			if (code != 200 && checkStatusCode) throw new Exception(urlConnection.getResponseMessage());
			BufferedReader reader = new BufferedReader(new InputStreamReader(code >= 400 ? urlConnection.getErrorStream() : urlConnection.getInputStream(), "utf-8"));
			String line = null;
			StringBuffer retSrc = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				retSrc.append(line);
			}
			return retSrc.toString();
		} catch (Exception ex) {
			throw ex;
		}
	}
	public static String getExecute(String url, Map<String, Object> params) throws Exception {
		String query = HttpInvoke.keyValues(params);
		if (StringUtil.isNotBlank(query)) {
			if (url.contains("?")) url = url + "&" + query;
			else url = url + "?" + query;
		}
		return execute("GET", url, null);
	}
	public static String postExecute(String url, Map<String, Object> body) throws Exception {
		String bodyStr = HttpInvoke.keyValues(body);
		return execute("POST", url, bodyStr);
	}
	public static String postExecute(String url, Map<String, Object> body, boolean checkStatusCode) throws Exception {
		String bodyStr = HttpInvoke.keyValues(body);
		return execute("POST", url, bodyStr, checkStatusCode);
	}

	public static String addKeyValue(String key, Object value) {
		try {
			return key + "=" + URLEncoder.encode(value.toString(), "UTF-8").replaceAll("\\+", "%20") + "&";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return key + "=" + value.toString() + "&";
		}
	}
	public static String keyValues(Object...keyValues) {
		String result = "";
		if (keyValues != null && keyValues.length > 1) {
			for (int i = 0; i < keyValues.length - 1; i += 2) {
				if (keyValues[i + 1] != null) {
					result += HttpInvoke.addKeyValue(keyValues[i].toString(), keyValues[i + 1].toString());
				} else {
					result += HttpInvoke.addKeyValue(keyValues[i].toString(), "");
				}
			}
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
	public static String keyValues(Map<String, Object> keyValues) {
		if (keyValues == null || keyValues.size() < 1)return null;
		String result = "";
		Set<String> keys = keyValues.keySet();
		for (String key : keys) {
			result += HttpInvoke.addKeyValue(key, keyValues.get(key));
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}
