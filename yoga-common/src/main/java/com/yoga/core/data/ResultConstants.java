package com.yoga.core.data;
/**
 * 返回状态
 * @author Skysea
 *
 */
public class ResultConstants {
	
	//SUCCEED////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 执行成功，没用任何警告信息
	 */
	public static final int RESULT_OK = 0;

	//WARNING////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 业务成功，但没有查询到匹配的数据
	 */
	public static final int WARNING_NO_DATA = 1;

	/**
	 * 业务成功，但因为没有分页参数导致查询到的数据过多，已经被后端截断部分结果
	 */
	public static final int WARNING_DATA_TRUNCATED = 2;
	
	//ERROR////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 未知错误，比如Java框架中被最终异常所捕获时返回
	 */
	public static final int ERROR_UNKNOWN = -1;
	
	/**
	 * 权限错误，比如未传递token或者token已经失效
	 */
	public static final int ERROR_FORBIDDEN = -2;
	
	/**
	 * 缺少参数，比如请求方传递的参数不够
	 */
	public static final int ERROR_MISSINGPARAM = -3;
	
	/**
	 * 参数错误，比如参数超出范围或者格式不合法
	 */
	public static final int ERROR_ILLEGALPARAM = -4;	

	/**
	 * 未登录
	 */
	public final static int ERROR_UNAUTHENTICAED = -5;	

	/**
	 * 未授权
	 */
	public final static int ERROR_UNAUTHORIZED = -6;
	
	/**
	 * 业务逻辑错误，比如流程错误或者接口被非法使用
	 */
	public static final int ERROR_BUSINESSERROR = -10;
}
