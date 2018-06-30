package com.yoga.imessager.friend.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class AcceptDto extends TenantDto {

	@NotNull(message = "请输入接受申请的用户ID")
	private Long friendId;
	private boolean allowMoment = true;
	private String alias;

	public Long getFriendId() {
		return friendId;
	}
	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public boolean isAllowMoment() {
		return allowMoment;
	}
	public void setAllowMoment(boolean allowMoment) {
		this.allowMoment = allowMoment;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
