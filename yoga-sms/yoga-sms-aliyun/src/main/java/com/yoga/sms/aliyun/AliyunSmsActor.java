package com.yoga.sms.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.sms.service.SmsActor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AliyunSmsActor implements SmsActor {

    private IAcsClient client = null;
    private String signName = null;
    private String templateCode = null;
    private String template = null;

    public boolean setup(String accessKeyId, String accessSecret, String signName, String templateCode) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("QuerySmsTemplate");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("TemplateCode", "SMS_187580369");
        try {
            CommonResponse response = client.getCommonResponse(request);
            String result = response.getData();
            JSONObject json = JSON.parseObject(result);
            this.template = json.getString("TemplateContent");
            if (StringUtil.isBlank(this.template)) throw new BusinessException("无效配置！");
            this.signName = signName;
            this.templateCode = templateCode;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String sendSms(String mobile, String content) {
        Pattern pattern = Pattern.compile("\\$\\{.+?}");
        Matcher matcher = pattern.matcher(template);
        int begin = 0;
        int valueBegin = 0;
        String prevCode = null;
        Map<String, String> params = new HashMap<>();
        while (matcher.find()) {
            String group = matcher.group();
            int end = matcher.start();
            String code = group.replace("${", "").replace("}", "");
            String left = template.substring(begin, end);
            int valueEnd = content.indexOf(left);
            if (valueEnd < 0) throw new BusinessException("短信内容与模板不匹配！");
            if (prevCode != null) {
                String value = content.substring(valueBegin, valueEnd);
                params.put(prevCode, value);
            }
            valueBegin = valueEnd + left.length();
            begin = end + group.length();
            prevCode = code;
        }
        if (prevCode != null) {
            String value;
            if (begin == template.length()) {
                value = content.substring(valueBegin);
            } else {
                int end = template.length();
                String left = template.substring(begin, end);
                int valueEnd = content.indexOf(left);
                if (valueEnd < 0) throw new BusinessException("短信内容与模板不匹配！");
                value = content.substring(valueBegin, valueEnd);
            }
            params.put(prevCode, value);
        }

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        if (params.size() > 0) {
            request.putQueryParameter("TemplateParam", JSON.toJSONString(params));
        }
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            JSONObject json = JSON.parseObject(response.getData());
            String code = json.getString("Code");
            if (code.equalsIgnoreCase("ok")) return null;
            String message = json.getString("Message");
            if (StringUtil.isNotBlank(message)) return message;
            return "发送短信失败";
        } catch (Exception e) {
            e.printStackTrace();
            return "发送短信失败";
        }
    }

    @Override
    public boolean isMatch(Map<String, String> configs) {
        return true;
    }
}
