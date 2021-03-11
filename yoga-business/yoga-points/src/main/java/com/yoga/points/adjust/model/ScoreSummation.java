package com.yoga.points.adjust.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ScoreSummation implements Serializable {

    @Id
    @Column(name = "user_id")
    private long userId;
    @Column(name = "points")
    private int points;
    @Column(name = "count")
    private int count;
}
