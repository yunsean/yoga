package com.yoga.massms;

import com.mascloud.sdkclient.Client;
import com.yoga.utility.sms.service.SmsActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MasSmsActor implements SmsActor {
    public static final Logger logger = LoggerFactory.getLogger(MasSmsActor.class);

    private Client client = Client.getInstance();

    private String sign = null;

    public boolean login(String url, String ecname, String userAccount, String password, String sign) {
        this.sign = sign;
        return client.login(url, userAccount, password, ecname);
    }

    @Override
    public String sendSms(String mobile, String content) {
        int result = client.sendDSMS(new String[]{mobile}, content, "", 1, sign, "", true);
        if (result == 1) return null;
        logger.error("Client.sendDSMS() failed, result=" + result);
        return "发送短信失败";
    }

    @Override
    public boolean isMatch(Map<String, String> configs) {
        return true;
    }
}
