package com.yoga.utility.district.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "system_district")
public class District {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "name")
    private String name;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "level")
    private Integer level;
    @Column(name = "initial")
    private String initial;
    @Column(name = "initials")
    private String initials;
    @Column(name = "pinyin")
    private String pinyin;
    @Column(name = "suffix")
    private String suffix;
    @Column(name = "code")
    private String code;
    @Column(name = "area_code")
    private String areaCode;
    @Column(name = "order")
    private Integer order;
}
