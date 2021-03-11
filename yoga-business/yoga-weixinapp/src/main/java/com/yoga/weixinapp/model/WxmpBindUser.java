package com.yoga.weixinapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "wxmp_bind_user")
public class WxmpBindUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String openid;
    @Column(name = "tenant_id")
    @Id
    private Long tenantId;
    @Column(name = "user_id")
    private Long userId;

    public WxmpBindUser(String openid, long tenantId, long userId) {
        this.openid = openid;
        this.tenantId = tenantId;
        this.userId = userId;
    }
}
