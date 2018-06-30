package com.yoga.imessager.user.model;

import com.yoga.imessager.group.enums.UserType;

import javax.persistence.*;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "findByTenantIdAndGroupId",
                query = "select u.id, u.tenant_id, u.token, u.nickname, u.avatar, gu.user_type from im_user u, im_group_user gu where u.id = gu.user_id and u.tenant_id = ?1 and gu.group_id = ?2 and gu.applying = 0",
                resultSetMapping = "ReturnUserAndType"
        )
})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "ReturnUserAndType",
                entities = {
                        @EntityResult(entityClass = UserAndType.class,
                                fields = {
                                        @FieldResult(name = "id", column = "id"),
                                        @FieldResult(name = "tenantId", column = "tenant_id"),
                                        @FieldResult(name = "nickname", column = "nickname"),
                                        @FieldResult(name = "token", column = "token"),
                                        @FieldResult(name = "avatar", column = "avatar"),
                                        @FieldResult(name = "userType", column = "user_type")
                                })

                }
        )
})
@Entity
public class UserAndType {

    @Id
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "nickname")
    private String nickname;
    private String token;
    private String avatar;
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
