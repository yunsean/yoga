package com.yoga.imessager.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yoga.core.annotation.Explain;
import com.yoga.imessager.group.model.Member;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PullBean {

    @Explain("群组ID")
    @NotNull(message = "群组ID不能为空")
    private long id;
    @Explain("群组成员列表")
    @JsonProperty("members")
    private List<Member> members;

    public static PullBean prototype() {
        PullBean dto = new PullBean();
        dto.id = 0;
        dto.members = new ArrayList<>();
        dto.members.add(new Member());
        return dto;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public List<Member> getMembers() {
        return members;
    }
    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
