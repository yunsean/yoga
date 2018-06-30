package com.yoga.core.sequence;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "g_sequence")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sequence implements Serializable {

    @Id
    private Long id;
    @Column(nullable = false, name="name")
    private String name;
    @Column(nullable = false, name="max")
    private Long max;
    @Column(nullable = false, name="length")
    private Long length;
    @Column(nullable = false, name="next")
    private Long next;
    @Column(nullable = true, name="rules")
    private String rules;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getMax() {
        return max;
    }
    public void setMax(Long max) {
        this.max = max;
    }

    public Long getLength() {
        return length;
    }
    public void setLength(Long length) {
        this.length = length;
    }

    public Long getNext() {
        return next;
    }
    public void setNext(Long next) {
        this.next = next;
    }

    public String getRules() {
        return rules;
    }
    public void setRules(String rules) {
        this.rules = rules;
    }
}
