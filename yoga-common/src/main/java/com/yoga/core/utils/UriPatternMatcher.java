package com.yoga.core.utils;

/**
 * uri匹配
 * @author Skysea
 *
 */
public class UriPatternMatcher {
	public static boolean matchesPattern(final String uri, final String pattern) {
		if(null == uri)
			return false;
		if (null == pattern)
			return true;
		if (uri.equals(pattern))
			return true;
	    if (pattern.equals("*")) {
	        return true;
	    } else {
	    	String newPattern = pattern.replace("**", "*");
	        return
	        (newPattern.endsWith("*") && uri.startsWith(newPattern.substring(0, newPattern.length() - 1))) ||
	        (newPattern.startsWith("*") && uri.endsWith(newPattern.substring(1, newPattern.length())));
	    }
	}
}
