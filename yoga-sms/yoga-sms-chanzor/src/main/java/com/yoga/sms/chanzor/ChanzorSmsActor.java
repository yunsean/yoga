package com.yoga.sms.chanzor;

import com.yoga.core.utils.HttpUtils;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.sms.service.SmsActor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class ChanzorSmsActor implements SmsActor {

    private String account;
    private String password;

    ChanzorSmsActor(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public String sendSms(String mobile, String content) {
        try {
            Map<String, Object> body = new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put("action", "send");
                    put("userid", "");
                    put("account", account);
                    put("password", password);
                    put("mobile", mobile);
                    put("content", content);
                }
            };
            String xml = HttpUtils.postExecute("http://sms.chanzor.com:8001/sms.aspx", body);
            JAXBContext context = JAXBContext.newInstance(SmsResult.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            SmsResult result = (SmsResult)unmarshaller.unmarshal(new StringReader(xml));
            if ("Success".equals(result.getReturnstatus())) return null;
            if (StringUtil.isBlank(result.getMessage())) return "Send Sms failed.";
            else return result.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    @Override
    public boolean isMatch(Map<String, String> configs) {
        return password.equals(configs.get("password"));
    }
}
