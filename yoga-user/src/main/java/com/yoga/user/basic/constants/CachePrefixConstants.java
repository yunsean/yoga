package com.yoga.user.basic.constants;
/**
 * 缓存前缀
 * @author Skysea
 *
 */
public class CachePrefixConstants {
	
	/**
	 * 角色缓存
	 */
	public static final String ROLE = "r_";
	
	/**
	 * 用户缓存
	 */
	public static final String USER = "u_";
	public static final String USER_UID = "uid_";
	/**
	 * 部门信息缓存
	 */
	public static final String DEPT = "d_";
	/**
	 * 权限缓存
	 */
	public static final String PRIVILEGE = "p_";
	
	/*
	 * 文章缓存
	 */
	//文章缓存
	public static final String ARTICAL = "'a_' + ";
	//文章二维码缓存
	public static final String ARTICAL_QRCODE = "a_qr_";
	//文章二维码缓存有效期
	public final static int ARTICAL_QRCODE_EXPIRE = 30;	//分钟

	/*
	 * 题库缓存
	 */
	//题库列表
	public static final String EXAMINELIST_PATTERN = "el_";
	public static final String EXAMINELIST = "'" + EXAMINELIST_PATTERN + "' + ";
	//题库详情
	public static final String EXAMINEITEM_PATTERN = "ei_";
	public static final String EXAMINEITEM = "'" + EXAMINEITEM_PATTERN + "' + ";
	/*
	 * 问题缓存
	 */
	//问题列表
	public static final String QUESTIONLIST_PATTERN = "ql_";
	public static final String QUESTIONLIST = "'" + QUESTIONLIST_PATTERN + "' + ";
	//问题详情
	public static final String QUESTIONITEM_PATTERN = "qi_";
	public static final String QUESTIONITEM = "'" + QUESTIONITEM_PATTERN + "' + ";

	public static final String LEAVE_REVIEW_PRIVILEGE = "'lrp_' + ";
}
