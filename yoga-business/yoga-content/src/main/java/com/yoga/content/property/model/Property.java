package com.yoga.content.property.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_property")
public class Property {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(nullable = false, name = "name")
	private String name;
	@Column(nullable = false, name = "parent_id")
	private Long parentId;
	@Column(nullable = true, name = "code")
	private String code;
	@Column(nullable = true, name = "poster")
	private String poster;

	@Transient
	private List<Property> children;

	public Property(long tenantId, String code, String name, long parentId, String poster) {
		super();
		this.tenantId = tenantId;
		this.name = name;
		this.parentId = parentId;
		this.code = code;
		this.poster = poster;
	}
	public void addChild(Property child) {
		if (this.children == null) this.children = new ArrayList<>();
		this.children.add(child);
	}
}
