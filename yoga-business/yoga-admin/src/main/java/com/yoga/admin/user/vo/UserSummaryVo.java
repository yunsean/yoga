package com.yoga.admin.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSummaryVo {

    private Long id;
    private String nickname;
    private String avatar;
    private String branch;
    private String duty;
}
