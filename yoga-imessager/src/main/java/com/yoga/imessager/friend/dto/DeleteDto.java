package com.yoga.imessager.friend.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DeleteDto extends TenantDto {

	@NotNull(message = "请输入需删除的用户ID")
	private Long friendId;

	public Long getFriendId() {
		return friendId;
	}
	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}
}
