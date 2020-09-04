package com.yoga.operator.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.model.LoginUser;
import com.yoga.operator.user.enums.GenderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.springframework.jdbc.core.RowMapper;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operator_user")
public class User implements Serializable, LoginUser {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	private String username;
	private String password;
	@Column(name = "branch_id")
	private Long branchId;
	@Column(name = "duty_id")
	private Long dutyId;
	private String nickname;
	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private GenderType gender;
	private String title;
	private String avatar;
	private String mobile;
	private String email;
	private String address;
	private String postcode;
	private String company;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "birthday")
	private Date birthday;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "update_time")
	private Date updateTime;
	@Column(name = "last_login")
	private Date lastLogin;
	private Boolean enabled;
	private Boolean deleted;

	@Transient
	private String branch;
	@Transient
	private String duty;
	@Transient
	private String roles;
	@Transient
	private Integer level;

	public User(long tenantId, String username, String password, Long branchId, Long dutyId, String nickname, GenderType gender, String title, String avatar, String mobile, String email, String address, String postcode, String company, Date birthday) {
		this.tenantId = tenantId;
		this.username = username;
		this.password = password;
		this.branchId = branchId;
		this.dutyId = dutyId;
		this.nickname = nickname;
		this.gender = gender;
		this.title = title;
		this.avatar = avatar;
		this.mobile = mobile;
		this.email = email;
		this.address = address;
		this.postcode = postcode;
		this.company = company;
		this.birthday = birthday;
		this.enabled = true;
		this.deleted = false;
		this.createTime = new Date();
		this.updateTime = this.createTime;
	}

	public String getNickname() {
		if (StringUtil.isBlank(nickname)) return username;
		else return nickname;
	}

	public static User getLoginUser() {
		return (User) SecurityUtils.getSubject().getSession().getAttribute("user");
	}

	@Override
	public String getName() {
		return nickname;
	}
}
