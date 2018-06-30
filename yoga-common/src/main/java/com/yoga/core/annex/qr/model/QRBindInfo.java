package com.yoga.core.annex.qr.model;

import java.io.Serializable;
import java.util.Map;

public class QRBindInfo implements Serializable {

	private String url;
	private Map<String, String> headers;
	private boolean transfer = false;

	public QRBindInfo() {

	}
	public QRBindInfo(String url, Map<String, String> headers) {
		this.url = url;
		this.headers = headers;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public boolean isTransfer() {
		return transfer;
	}
	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}
}
