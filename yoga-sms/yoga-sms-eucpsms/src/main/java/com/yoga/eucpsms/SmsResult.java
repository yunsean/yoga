package com.yoga.eucpsms;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class SmsResult implements Serializable {
	private static final long serialVersionUID = -755135633544870195L;

	private long error;
	private String message;	

	@XmlElement(name="error")
	public long getError() {
		return error;
	}
	public void setError(long error) {
		this.error = error;
	}

	@XmlElement(name="message")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
