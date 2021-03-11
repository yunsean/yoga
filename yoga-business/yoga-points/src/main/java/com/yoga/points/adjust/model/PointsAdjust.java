package com.yoga.points.adjust.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "points_adjust")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsAdjust implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "add_time")
    private LocalDateTime addTime;
    @Column(name = "points")
    private Integer points;
    @Column(name = "reason")
    private String reason;

    @Column(name = "submitter_id")
    private Long submitterId;

    @Transient
    private String nickname;
    @Transient
    private String submitter;
    @Transient
    private String branch;
    @Transient
    private String duty;

    public PointsAdjust(long tenantId, long userId, LocalDateTime addTime, int points, String reason, long submitterId) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.addTime = addTime;
        this.points = points;
        this.reason = reason;
        this.submitterId = submitterId;
    }
}
