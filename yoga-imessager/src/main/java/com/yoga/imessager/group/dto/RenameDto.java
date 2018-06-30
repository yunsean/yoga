package com.yoga.imessager.group.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class RenameDto extends TenantDto {

    @Explain("群组ID")
    @NotNull(message = "群组ID不能为空")
    private Long id;
    @Explain("群组名称")
    private String name;
    @Explain("群组图标")
    private String avatar;

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

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
