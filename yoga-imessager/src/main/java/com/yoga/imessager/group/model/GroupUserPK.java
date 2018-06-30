package com.yoga.imessager.group.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@JsonInclude(Include.NON_NULL)
public class GroupUserPK implements Serializable {
	@Id
	@Column(name = "group_id")
	private Long groupId = null;

	@Id
	@Column(name = "user_id")
	private Long userId = null;

	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
