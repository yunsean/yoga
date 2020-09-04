package com.yoga.massms;


import com.yoga.core.data.ChainMap;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.sms.service.SmsActor;
import com.yoga.utility.sms.service.SmsFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MasSmsFactory implements SmsFactory {

    @Override
    public String getName() {
        return "中国移动短信网关";
    }

    private Map<String, MasSmsActor> smsActoies = new HashMap<>();

    @Override
    public SmsActor build(Map<String, String> configs) {
        String ecname = configs.get("ecname");
        String userAccount = configs.get("userAccount");
        if (StringUtil.hasBlank(ecname, userAccount)) return null;
        MasSmsActor smsActor = smsActoies.get(ecname + "." + userAccount);
        if (smsActor != null) return smsActor;
        String password = configs.get("password");
        String url = configs.get("url");
        String sign = configs.get("sign");
        if (StringUtil.hasBlank(password, url, sign)) return null;
        smsActor = new MasSmsActor();
        if (!smsActor.login(url, ecname, userAccount, password, sign)) return null;
        smsActoies.put(ecname + "." + userAccount, smsActor);
        return smsActor;
    }

    @Override
    public Map<String, String> configItems() {
        return new ChainMap<String, String>()
                .set("url", "身份认证地址")
                .set("userAccount", "用户登录帐号")
                .set("password", "用户登录密码")
                .set("ecname", "用户企业名称")
                .set("sign", "签名编码");
    }
}
