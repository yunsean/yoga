package com.yoga.moment.group.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "moment_group")
public class MomentGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "name")
    private String name;
    @Column(name = "remark")
    private String remark;
    @Column(name = "deleted")
    private Boolean deleted;

    @Transient
    private Integer userCount;
    @Transient
    private String lastMessage;

    public MomentGroup(Long tenantId, String name, String remark) {
        this.tenantId = tenantId;
        this.name = name;
        this.remark = remark;
        this.deleted = false;
    }
}