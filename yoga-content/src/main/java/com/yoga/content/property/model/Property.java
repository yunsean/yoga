package com.yoga.content.property.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yoga.core.utils.TypeCastUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "cms_property")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "DeletePropertyTreeById", procedureName = "DeletePropertyTreeById", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "parentid", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "containParent", type = Boolean.class) }) })
public class Property {

	@Id
	private long id;
	@Column(name = "tenant_id")
	private long tenantId;

	@Column(nullable = false, name = "name")
	private String name;
	@Column(nullable = false, name = "parent_id")
	private long parentId;

	@Column(nullable = true, name = "code")
	private String code;
	
	@Transient
	private String value;
	@Transient
	private List<Property> children;
		
	public Property() {
		super();
	}
	public Property(long tenantId, String name, long parentId, String code) {
		super();
		this.tenantId = tenantId;
		this.name = name;
		this.parentId = parentId;
		this.code = code;
	}
	//TODO: delete it
	public Property(Map<String, Object> row) {
		super();
		this.id = TypeCastUtil.toLong(row.get("id"));
		this.name = TypeCastUtil.toString(row.get("name"));
		this.parentId = TypeCastUtil.toLong(row.get("parent_id"));
		this.code = TypeCastUtil.toString(row.get("code"));
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}	
	
	public String getValue() {
		if (value == null) return "";
		else return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public List<Property> getChildren() {
		return children;
	}
	public void setChildren(List<Property> children) {
		this.children = children;
	}
	public void addChild(Property optionen) {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		this.children.add(optionen);
	}
}
