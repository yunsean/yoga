package com.yoga.logging.model;

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
@Table(name = "system_logging")
@Entity(name = "Logging")
public class Logging {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "log_id")
    private Long logId;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "module")
    private String module;
    @Column(name = "method")
    private String method;
    @Column(name = "primary_id")
    private String primaryId;
    @Column(name = "primary_info")
    private String primaryInfo;
    @Column(name = "description")
    private String description;
    @Column(name = "detail")
    private String detail;
    @Column(name = "result")
    private String result;

    public Logging(Long tenantId, Long userId, String username, String module, String method, String primaryId, String primaryInfo, String description, String detail, String result) {
        this.tenantId = tenantId;
        this.createTime = new Date();
        this.userId = userId;
        this.username = username;
        this.module = module;
        this.method = method;
        this.primaryId = primaryId;
        this.primaryInfo = primaryInfo;
        this.description = description;
        this.detail = detail;
        this.result = result;
    }
}
