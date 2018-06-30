package com.yoga.core.interfaces.sms;

public interface SmsActor {

    String sendSms(String mobile, String content);  //return null is succeed, otherwise return an error description.
}
