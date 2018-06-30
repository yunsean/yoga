package com.yoga.core.utils;

import java.math.BigDecimal;
import java.util.Date;

import com.yoga.core.data.BaseEnum;
/**
 * 缺省值设置
 * @author Skysea
 *
 */
public class DefaultValue {
	
	public static <T> T getEnumValue(BaseEnum<T> src, BaseEnum<T> defaultValue){
		if(null == src){
			return null == defaultValue ? null : defaultValue.getCode();
		}
		return src.getCode();
	}
	
	public static <T> T getEnumValue(BaseEnum<T> src){
		return getEnumValue(src, null);
	}
	
	public static long getMoneyValue(Money src, long defaultValue){
		return null == src ? defaultValue : src.getCent();
	}
	public static long getMoneyValue(Money src){
		return getMoneyValue(src, 0);
	}
	public static BigDecimal getMoneyAmount(Money src, BigDecimal defaultValue){
		return null == src ? defaultValue : src.getAmount();
	}
	public static BigDecimal getMoneyAmount(Money src){
		return getMoneyAmount(src, new BigDecimal("0.0"));
	}
	public static Date getDateValue(Date src){
		return null == src ? new Date(0) : src;
	}
	
	public static Date getDateValue(Date src, Date defaultValue){
		return null == src ? defaultValue : src;
	}
	public static long getTimeValue(Long src){
		return null == src ? 0 : src;
	}
	
	public static long getTimeValue(Long src, long defaultValue){
		return null == src ? defaultValue : src;
	}
	
	public static long getLongValue(Long src, long defaultValue){
		return null == src ? defaultValue : src;
	}
	
	public static long getLongValue(Long src){
		return getLongValue(src, 0L);
	}
	
	public static int getInt(Integer src, int defaultValue){
		return null == src ? defaultValue : src;
	}
	
	public static int getInt(Integer src){
		return getInt(src, 0);
	}
}
