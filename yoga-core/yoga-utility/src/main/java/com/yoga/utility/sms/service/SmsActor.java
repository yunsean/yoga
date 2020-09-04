package com.yoga.utility.sms.service;

import java.util.Map;

public interface SmsActor {

    String sendSms(String mobile, String content);  //return null is succeed, otherwise return an error description.
    boolean isMatch(Map<String, String> configs);
}
