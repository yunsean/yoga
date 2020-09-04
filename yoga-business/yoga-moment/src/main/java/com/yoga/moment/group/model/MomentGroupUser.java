package com.yoga.moment.group.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "moment_group_user")
public class MomentGroupUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "group_id")
    private Long groupId;
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Transient
    private String nickname;
    @Transient
    private String avatar;
    @Transient
    private String branch;

    public MomentGroupUser(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }
}