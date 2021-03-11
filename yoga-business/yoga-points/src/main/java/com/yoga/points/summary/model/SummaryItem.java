package com.yoga.points.summary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryItem {

    private Long userId;
    private Integer points;
    private String detail;
}
