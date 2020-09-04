package com.yoga.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

	public static int intValue(String value) {
		return intValue(value, 0);
	}
	public static int intValue(String value, int def) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return def;
		}
	}
	public static Integer optInt(Object value) {
		if (value == null) return null;
		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			return null;
		}
	}

	public static float floatValue(String value) {
		return floatValue(value, 0F);
	}
	public static float floatValue(String value, float def) {
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return def;
		}
	}
	public static double doubleValue(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return .0;
		}
	}
	public static long longValue(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return 0;
		}
	}
	public static Long optLong(Object value) {
		if (value == null) return null;
		try {
			return Long.parseLong(value.toString());
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isPureInt(String value) {
		Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
		Matcher matcher = pattern.matcher((CharSequence)value);
		return matcher.matches();
	}

	public static  <T> boolean isEqual(T lval, T rval) {
		if (lval == null && rval == null) return true;
		if (lval == null || rval == null) return false;
		if (lval.equals(rval)) return true;
		else return false;
	}
}
