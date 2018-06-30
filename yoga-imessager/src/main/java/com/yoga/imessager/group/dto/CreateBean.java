package com.yoga.imessager.group.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yoga.core.annotation.Explain;
import com.yoga.imessager.group.model.Member;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class CreateBean {

    @Explain("群名称")
    @NotEmpty(message = "群组名不能为空")
    private String name;
    @Explain("群组图标")
    private String avatar;
    @Explain("添加租户所有人员")
    private Boolean allUser = false;
    @Explain("群组成员列表")
    @JsonProperty("members")
    private List<Member> members;

    public static CreateBean prototype() {
        CreateBean dto = new CreateBean();
        dto.name = dto.avatar = "";
        dto.allUser = false;
        dto.members = new ArrayList<>();
        dto.members.add(new Member());
        return dto;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Member> getMembers() {
        return members;
    }
    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Boolean isAllUser() {
        return allUser;
    }
    public void setAllUser(Boolean allUser) {
        this.allUser = allUser;
    }
}
