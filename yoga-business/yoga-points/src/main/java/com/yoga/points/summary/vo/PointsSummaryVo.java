package com.yoga.points.summary.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PointsSummaryVo {

    private Long id;
    private Long userId;
    private String nickname;
    private String branch;
    private String duty;
    private Integer year;
    private Float points;
    private Integer yearRank;
    private Integer dutyRank;
    private Float penalty;
    private String detail;
}
