package com.yoga.yunpian;


import com.alibaba.fastjson.JSON;
import com.yoga.core.interfaces.sms.SmsActor;
import com.yoga.core.utils.HttpInvoke;
import com.yoga.core.utils.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class YunpianSmsActor implements SmsActor {
    private String apiKey = null;
    public YunpianSmsActor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String sendSms(String mobile, String content) {
        try {
            Map<String, Object> body = new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put("apikey", apiKey);
                    put("mobile", mobile);
                    put("text", content);
                }
            };
            String json = HttpInvoke.postExecute("http://sms.yunpian.com/v2/sms/single_send.json", body, false);
            SmsResult result = JSON.parseObject(json, SmsResult.class);
            if (result.getCode() == 0) return null;
            String message = result.getMsg();
            if (StrUtil.isBlank(message)) message = "发送短信错误！";
            return message;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}
