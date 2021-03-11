package com.yoga.weixinapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "wxmp_send_subscribe")
public class WxmpSendMessage {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "template_id")
    private String templateId;
    @Column(name = "page")
    private String page;
    @Column(name = "data")
    private String data;
    @Column(name = "add_time")
    private LocalDateTime addTime;
    @Column(name = "send")
    private Boolean send;
    @Column(name = "send_time")
    private LocalDateTime sendTime;
    @Column(name = "message")
    private String message;

    public WxmpSendMessage(Long tenantId, Long userId, String templateId, String page, String data) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.templateId = templateId;
        this.page = page;
        this.data = data;
        this.addTime = LocalDateTime.now();
        this.send = false;
    }
    public WxmpSendMessage(Long id, String message) {
        this.id = id;
        this.send = true;
        this.sendTime = LocalDateTime.now();
        this.message = message;
    }
}
