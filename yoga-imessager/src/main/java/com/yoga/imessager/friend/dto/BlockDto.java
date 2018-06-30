package com.yoga.imessager.friend.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class BlockDto extends TenantDto {

	@NotNull(message = "请输入被阻止的用户ID")
	private Long friendId;
	private boolean block;

	public Long getFriendId() {
		return friendId;
	}
	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public boolean isBlock() {
		return block;
	}
	public void setBlock(boolean block) {
		this.block = block;
	}
}
