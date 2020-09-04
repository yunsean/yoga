package com.yoga.utility.push.model;

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
@Table(name = "system_push")
@Entity(name = "PushUser")
public class PushUser {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "push_channel")
    private String pushChannel;
    @Column(name = "message_count")
    private Integer messageCount;
}
