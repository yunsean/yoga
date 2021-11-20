package com.yoga.sms.chanzor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name="returnsms")
public class SmsResult implements Serializable {
	private static final long serialVersionUID = -755135633544870195L;

	private String returnstatus;
	private String message;
	private int remainpoint;
	private int successCounts;
	private int taskID;
}
