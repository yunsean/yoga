package com.yoga.admin.aggregate.ao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;
    private String module;
    private String name;
    private String icon;
    private String value;
    private String units;
    private String url;
    private String image;
    private Integer sort = 0;

    public Schedule(String module, String name, String icon, int count, String units, String url) {
        this.module = module;
        this.name = name;
        this.icon = icon;
        this.value = String.valueOf(count);
        this.units = units;
        this.url = url;
    }
    public Schedule(String module, String name, String icon, String value, String units, String url) {
        this.module = module;
        this.name = name;
        this.icon = icon;
        this.value = value;
        this.units = units;
        this.url = url;
    }
}
