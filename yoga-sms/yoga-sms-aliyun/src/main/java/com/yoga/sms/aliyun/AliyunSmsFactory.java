package com.yoga.sms.aliyun;

import com.yoga.core.data.ChainMap;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.sms.service.SmsActor;
import com.yoga.utility.sms.service.SmsFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AliyunSmsFactory implements SmsFactory {

    @Override
    public String getName() {
        return "阿里云短信网关";
    }

    private Map<String, AliyunSmsActor> smsActoies = new HashMap<>();

    @Override
    public SmsActor build(Map<String, String> configs) {
        String accessKeyId = configs.get("accessKeyId");
        String signName = configs.get("signName");
        String templateCode = configs.get("templateCode");
        if (StringUtil.hasBlank(accessKeyId, signName, templateCode)) return null;
        AliyunSmsActor smsActor = smsActoies.get(accessKeyId + "." + signName + "." + templateCode);
        if (smsActor != null) return smsActor;
        String accessSecret = configs.get("accessSecret");
        if (StringUtil.hasBlank(accessSecret)) return null;
        smsActor = new AliyunSmsActor();
        if (!smsActor.setup(accessKeyId, accessSecret, signName, templateCode)) return null;
        smsActoies.put(accessKeyId + "." + signName + "." + templateCode, smsActor);
        return smsActor;
    }

    @Override
    public Map<String, String> configItems() {
        return new ChainMap<String, String>()
                .set("accessKeyId", "Access Key")
                .set("accessSecret", "Access Secret")
                .set("templateCode", "模板编码")
                .set("signName", "断线签名");
    }
}
