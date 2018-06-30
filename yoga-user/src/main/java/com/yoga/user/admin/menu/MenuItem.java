package com.yoga.user.admin.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Cloneable{

	private long menuId;		//仅自定义菜单使用
	private int sort;
	private String code;
	private String icon;
	private String name;
	private String url;
	private String remark;
	private boolean admin;
	private boolean checked;
	private List<MenuItem> children;

	public MenuItem(int sort, String code, String icon, String name, String url, String remark, boolean admin) {
		this.sort = sort;
		this.code = code;
		this.icon = icon;
		this.name = name;
		this.url = url;
		this.remark = remark;
		this.admin = admin;
	}
	public MenuItem(int sort, String code, String name, String url, String remark, boolean checked) {
		this.sort = sort;
		this.icon = "icon-star";
		this.code = code;
		this.name = name;
		this.url = url;
		this.remark = remark;
		this.checked = checked;
	}
	public MenuItem(long menuId, int sort, String code, String name, String url, String remark, boolean checked) {
		this.menuId = menuId;
		this.sort = sort;
		this.code = code;
		this.name = name;
		this.url = url;
		this.remark = remark;
		this.checked = checked;
	}

	public MenuItem(String name, List<MenuItem> children) {
		this.name = name;
		this.icon = "icon-star";
		this.children = children;
	}
	public MenuItem(MenuItem m){
		this.sort = m.sort;
		this.code = m.code;
		this.icon = m.icon;
		this.name = m.name;
		this.url = m.url;
		this.remark = m.remark;
		this.admin = m.admin;
	}

	public long getMenuId() {
		return menuId;
	}
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean hasChildren() {
		return children != null && children.size() > 0;
	}
	public List<MenuItem> getChildren() {
		return children;
	}
	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}
	public void addChild(MenuItem children) {
		if (this.children == null) this.children = new ArrayList<>();
		this.children.add(children);
	}
	public void addChild(List<MenuItem> children) {
		if (this.children == null) this.children = new ArrayList<>();
		this.children.addAll(children);
	}
}
