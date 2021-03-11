package com.yoga.points.summary.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "points_year")
@JsonInclude(Include.NON_NULL)
public class PointsYear implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "year")
    private Integer year;

    @Column(name = "begin_date")
    private LocalDate beginDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "warning_user_ids")
    private String warningUserIds;

    public PointsYear(long tenantId, int year, LocalDate beginDate, LocalDate endDate) {
        this.tenantId = tenantId;
        this.year = year;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.updateTime = null;
    }
    public PointsYear(Long id, LocalDateTime updateTime) {
        this.id = id;
        this.updateTime = updateTime;
    }
    public PointsYear(Long id, String warningUserIds) {
        this.id = id;
        this.warningUserIds = warningUserIds;
    }
}
