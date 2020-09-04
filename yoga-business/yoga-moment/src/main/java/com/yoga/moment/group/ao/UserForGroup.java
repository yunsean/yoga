package com.yoga.moment.group.ao;

import com.yoga.operator.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
public class UserForGroup extends User {
    @Transient
    private Boolean included;
}
