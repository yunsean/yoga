package com.yoga.points.summary.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummarySetting implements Serializable {

	@JSONField
	private int annualNum;
	@JSONField
	private int weekAt;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
