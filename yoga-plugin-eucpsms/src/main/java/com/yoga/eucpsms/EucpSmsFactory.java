package com.yoga.eucpsms;


import com.yoga.core.interfaces.sms.SmsActor;
import com.yoga.core.interfaces.sms.SmsFactory;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EucpSmsFactory implements SmsFactory {

    @Override
    public String getName() {
        return "EUCP短信网关";
    }

    private Map<String, SmsActor> smsActoies = new HashMap<>();

    @Override
    public SmsActor build(Map<String, String> configs) {
        String cdkey = configs.get("cdkey");
        if (StrUtil.isBlank(cdkey)) return null;
        SmsActor smsActor = smsActoies.get(cdkey);
        if (smsActor != null) return smsActor;
        String password = configs.get("password");
        String url = configs.get("url");
        smsActor = new EucpSmsActor(cdkey, password, url);
        smsActoies.put(cdkey, smsActor);
        return smsActor;
    }

    @Override
    public Map<String, String> configItems() {
        return new MapConverter.MapItem<String, String>()
                .set("cdkey", "企业账号")
                .set("password", "账号密码")
                .set("url", "网关地址");
    }
}
