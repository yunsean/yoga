package com.yoga.eucpsms;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yoga.core.base.BaseService;
import com.yoga.core.data.ChainMap;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.sms.service.SmsActor;
import com.yoga.utility.sms.service.SmsFactory;
import com.yoga.utility.sms.service.SmsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EucpSmsFactory implements SmsFactory {
    public static final Logger logger = LoggerFactory.getLogger(EucpSmsFactory.class);

    @Override
    public String getName() {
        return "EUCP短信网关";
    }

    private Map<String, SmsActor> smsActoies = new HashMap<>();

    @Override
    public SmsActor build(Map<String, String> configs) {
        String cdkey = configs.get("cdkey");
        if (StringUtil.isBlank(cdkey)) return null;
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
        return new ChainMap<String, String>()
                .set("cdkey", "企业账号")
                .set("password", "账号密码")
                .set("url", "网关地址");
    }
}
