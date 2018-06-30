package com.yoga.imessager.friend.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class ModifyDto extends TenantDto {

	@NotNull(message = "请输入修改信息的用户ID")
	private Long friendId;
	private Boolean allowMoment;
	private String alias;

	public Long getFriendId() {
		return friendId;
	}
	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public Boolean getAllowMoment() {
		return allowMoment;
	}
	public void setAllowMoment(Boolean allowMoment) {
		this.allowMoment = allowMoment;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
