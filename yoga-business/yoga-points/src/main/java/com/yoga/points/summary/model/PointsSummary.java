package com.yoga.points.summary.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yoga.excelkit.annotation.Excel;
import com.yoga.excelkit.annotation.ExcelField;
import com.yoga.points.summary.converter.ExcelPointsConvertor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Excel("积分汇总")
@Table(name = "points_summary")
@JsonInclude(Include.NON_NULL)
public class PointsSummary implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "user_id")
    private Long userId;

    @Transient
    @ExcelField(value = "用户", width = 100)
    private String nickname;
    @Transient
    @ExcelField(value = "部门", width = 100)
    private String branch;
    @Transient
    @ExcelField(value = "职级", width = 100)
    private String duty;

    @Column(name = "year")
    private Integer year;
    @Column(name = "points")
    @ExcelField(value = "当前积分", width = 100, writeConverter = ExcelPointsConvertor.class)
    private Integer points;
    @Column(name = "penalty")
    @ExcelField(value = "被扣积分", width = 100, writeConverter = ExcelPointsConvertor.class)
    private Integer penalty;
    @Column(name = "year_rank")
    @ExcelField(value = "总排名", width = 100)
    private Integer yearRank;
    @Column(name = "duty_rank")
    @ExcelField(value = "同职级排名", width = 100)
    private Integer dutyRank;
    @Column(name = "branch_rank")
    @ExcelField(value = "部门排名", width = 100)
    private Integer branchRank;
    @ExcelField(value = "积分详情", width = 500)
    @Column(name = "detail")
    private String detail;

    public PointsSummary(long tenantId, long userId, int year, int points, int penalty, String detail) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.year = year;
        this.points = points;
        this.yearRank = 1;
        this.dutyRank = 1;
        this.penalty = penalty;
        this.detail = detail;
    }
}
