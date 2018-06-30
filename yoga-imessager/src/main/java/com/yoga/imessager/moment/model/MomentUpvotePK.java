package com.yoga.imessager.moment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@JsonInclude(Include.NON_NULL)
public class MomentUpvotePK implements Serializable {
	@Id
	@Column(name = "moment_id")
	private long momentId;
	@Id
	@Column(name = "upvoter_id")
	private Long upvoterId;

	public long getMomentId() {
		return momentId;
	}
	public void setMomentId(long momentId) {
		this.momentId = momentId;
	}

	public Long getUpvoterId() {
		return upvoterId;
	}
	public void setUpvoterId(Long upvoterId) {
		this.upvoterId = upvoterId;
	}
}
