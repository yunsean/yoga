package com.yoga.weixinapp.wxapi;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxBaseResult {
    private int errcode = 0;
    @SerializedName("errmsg")
    private String errMsg;
}
