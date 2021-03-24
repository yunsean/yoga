package com.yoga.admin.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterUserVo {

	private Long id;
	private String username;
	private Long branchId;
	private Long dutyId;
	private String nickname;
	private String branch;
	private String duty;
	private Integer level;
	private String avatar;
}
