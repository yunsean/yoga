package com.yoga.weixinapp.wxapi;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import com.yoga.weixinapp.ao.WxmpDataItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxSendSubscribeRequest {
    @SerializedName("touser")
    private String openid;
    @SerializedName("template_id")
    private String templateId;
    @SerializedName("page")
    private String page;
    private Map<String, WxmpDataItem> data;
    @SerializedName("miniprogram_state")
    private String state = "formal";    //developer为开发版；trial为体验版；formal为正式版
    private String lang = "zh_CN(";     //zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)

    public WxSendSubscribeRequest(String openid, String templateId, String page, Map<String, WxmpDataItem> data, String state) {
        this.openid = openid;
        this.templateId = templateId;
        this.page = page;
        this.data = data;
        this.state = state;
    }
}
