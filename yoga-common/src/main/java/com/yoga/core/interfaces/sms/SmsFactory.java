package com.yoga.core.interfaces.sms;

import java.util.Map;

public interface SmsFactory {

    String getName();
    SmsActor build(Map<String, String> configs);    //in    Config Key & Config Value
    Map<String, String> configItems();              //out   Config key & Readable name
}
