package com.yoga.yunpian;


import com.yoga.core.interfaces.sms.SmsActor;
import com.yoga.core.interfaces.sms.SmsFactory;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class YunpianSmsFactory implements SmsFactory {

    @Override
    public String getName() {
        return "云片网短信网关";
    }

    private Map<String, YunpianSmsActor> smsActoies = new HashMap<>();

    @Override
    public SmsActor build(Map<String, String> configs) {
        String apikey = configs.get("apikey");
        if (StrUtil.hasBlank(apikey)) return null;
        YunpianSmsActor smsActor = smsActoies.get(apikey);
        if (smsActor != null) return smsActor;
        smsActor = new YunpianSmsActor(apikey);
        smsActoies.put(apikey, smsActor);
        return smsActor;
    }

    @Override
    public Map<String, String> configItems() {
        return new MapConverter.MapItem<String, String>()
                .set("apikey", "APIKEY");
    }
}
