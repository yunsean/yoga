package com.yoga.imessager.friend.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class ApplyDto extends TenantDto {

	@NotNull(message = "请输入申请的用户ID")
	private Long friendId;
	private boolean allowMoment = true;
	private String alias;
	@NotNull(message = "请输入申请消息")
	private String hello;

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

	public String getHello() {
		return hello;
	}
	public void setHello(String hello) {
		this.hello = hello;
	}
}
