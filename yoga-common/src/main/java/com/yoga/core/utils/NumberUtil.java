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
	public static float floatValue(String value) {
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return .0f;
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

	public static boolean isPureInt(String value) {
		Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
		Matcher matcher = pattern.matcher((CharSequence)value);
		return matcher.matches();
	}
}
