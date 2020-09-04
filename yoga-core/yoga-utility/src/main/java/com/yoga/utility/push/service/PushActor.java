package com.yoga.utility.push.service;

import java.util.List;
import java.util.Map;

public interface PushActor {

    String getCode();
    int getPriority();
    boolean supportDeviceModel(long tenantId, String deviceModel);
    boolean pushToSingle(long tenantId, String clientId, String title, String content, String url, Map<String, String> extras);
    boolean pushToMultiple(long tenantId, List<String> clientIds, String title, String content, String url, Map<String, String> extras);
    boolean pushToBroadcast(long tenantId, List<String> tags, String title, String content, String url, Map<String, String> extras);
}
