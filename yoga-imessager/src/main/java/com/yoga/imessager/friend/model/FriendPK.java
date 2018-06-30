package com.yoga.imessager.friend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@JsonInclude(Include.NON_NULL)
public class FriendPK implements Serializable {
	@Id
	@Column(name = "user_id")
	private long userId;

	@Id
	@Column(name = "friend_id")
	private long friendId;

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getFriendId() {
		return friendId;
	}
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
}
