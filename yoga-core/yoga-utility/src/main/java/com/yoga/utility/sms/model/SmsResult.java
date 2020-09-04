package com.yoga.utility.sms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_sms")
@Entity(name = "SmsResult")
public class SmsResult {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "sms_id")
    private Long smsId;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "action")
    private String action;
    @Column(name = "content")
    private String content;
    @Column(name = "result")
    private Boolean result;
    @Column(name = "message")
    private String message;
    @Column(name = "send_time")
    private Date sendTime;

    public SmsResult(Long tenantId, String mobile, String action, String content, boolean result, String message) {
        this.tenantId = tenantId;
        this.mobile = mobile;
        this.action = action;
        this.content = content;
        this.result = result;
        this.message = message;
        this.sendTime = new Date();
    }
}
