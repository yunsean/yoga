package com.yoga.sms.chanzor;

import com.yoga.core.data.ChainMap;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.sms.service.SmsActor;
import com.yoga.utility.sms.service.SmsFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChanzorSmsFactory implements SmsFactory {

    @Override
    public String getName() {
        return "畅卓短信网关";
    }

    private Map<String, SmsActor> smsActoies = new HashMap<>();

    @Override
    public SmsActor build(Map<String, String> configs) {
        String account = configs.get("account");
        if (StringUtil.isBlank(account)) return null;
        SmsActor smsActor = smsActoies.get(account);
        if (smsActor != null && smsActor.isMatch(configs)) return smsActor;
        String password = configs.get("password");
        smsActor = new ChanzorSmsActor(account, password);
        smsActoies.put(account, smsActor);
        return smsActor;
    }

    @Override
    public Map<String, String> configItems() {
        return new ChainMap<String, String>()
                .set("account", "账号")
                .set("password", "密码");
    }
}
