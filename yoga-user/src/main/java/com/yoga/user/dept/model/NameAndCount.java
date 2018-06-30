package com.yoga.user.dept.model;

import javax.persistence.*;

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "ReturnDeptNameAndCount",
                entities = {
                        @EntityResult(entityClass = NameAndCount.class)
                }
        )
})
@Entity(name = "deptNameAndCount")
public class NameAndCount {

    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "count")
    private Integer count;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
}
