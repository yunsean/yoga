package com.yoga.imessager.friend.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class RejectDto extends TenantDto {

	@NotNull(message = "请输入被拒绝的用户ID")
	private Long friendId;
	@NotNull(message = "请输入拒绝原因")
	private String reason;

	public Long getFriendId() {
		return friendId;
	}
	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
