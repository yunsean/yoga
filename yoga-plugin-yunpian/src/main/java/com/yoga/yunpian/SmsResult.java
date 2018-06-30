package com.yoga.yunpian;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="response")
public class SmsResult implements Serializable {
	private static final long serialVersionUID = -755135633544870195L;

	@JSONField
	private int code;
	@JSONField
	private String msg;
	@JSONField
	private int count;
	@JSONField
	private double fee;
	@JSONField
	private String unit;
	@JSONField
	private String mobile;
	@JSONField
	private long sid;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}
}
