package com.yoga.ewedding.recharge.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class AlipayNotifyBean {

    @JSONField(name = "notify_time")
    private Date notifyTime;
    @JSONField(name = "notify_type")
    private String notifyType;
    @JSONField(name = "notify_id")
    private String notifyId;
}
