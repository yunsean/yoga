package com.yoga.eucpsms;


import com.yoga.core.interfaces.sms.SmsActor;
import com.yoga.core.utils.HttpInvoke;
import com.yoga.core.utils.StrUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class EucpSmsActor implements SmsActor {

    private String cdkey;
    private String password;
    private String url;

    public EucpSmsActor(String cdkey, String password, String url) {
        this.cdkey = cdkey;
        this.password = password;
        this.url = url;
    }

    @Override
    public String sendSms(String mobile, String content) {
        try {
            Map<String, Object> body = new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put("cdkey", cdkey);
                    put("password", password);
                    put("phone", mobile);
                    put("message", content);
                }
            };
            String xml = HttpInvoke.postExecute(this.url, body);
            JAXBContext context = JAXBContext.newInstance(SmsResult.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            SmsResult result = (SmsResult)unmarshaller.unmarshal(new StringReader(xml));
            if (result.getError() == 0) return null;
            if (StrUtil.isBlank(result.getMessage())) return "Send Sms failed.";
            else return result.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}
