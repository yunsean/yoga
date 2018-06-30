package com.yoga.user.duty.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddDutyDto extends TenantDto {

    private Long belowId;

    @NotNull(message = "职务名称不能为空")
    @Size(min = 1, max = 20, message = "职务名称长度需要在1~20个字符之间")
    private String name;

    private String remark;

    private long[] roleIds;

    public Long getBelowId() {
        return belowId;
    }

    public void setBelowId(Long belowId) {
        this.belowId = belowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(long[] roleIds) {
        this.roleIds = roleIds;
    }
}
