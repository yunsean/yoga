package com.yoga.points.summary.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class MyPointsSummaryVo {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
