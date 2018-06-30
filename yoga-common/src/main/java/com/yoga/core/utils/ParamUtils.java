package com.yoga.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class ParamUtils {

    public ParamUtils() {
    }
    public static Money getMoneyParameter(HttpServletRequest request, String name) {
        String m = getParameter(request, name, false);
        try{
        return new Money(m);
        }catch(Exception e){
            
        }
        return new Money();
    }
    public static String getParameter(HttpServletRequest request, String name) {
        return getParameter(request, name, false);
    }

    public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
        return getParameter(request, name, defaultValue, false);
    }

    public static String getParameter(HttpServletRequest request, String name,
                                      boolean emptyStringsOK) {
        return getParameter(request, name, null, emptyStringsOK);
    }

    public static String getParameter(HttpServletRequest request, String name, String defaultValue,
                                      boolean emptyStringsOK) {
        String temp = request.getParameter(name);

        if (temp != null) {
            if (temp.equals("") && !emptyStringsOK) {
                return defaultValue;
            } else {
                return temp;
            }
        } else {
            return defaultValue;
        }
    }

    public static String[] getParameters(HttpServletRequest request, String name) {
        if (name == null) {
            return new String[0];
        }
        String paramValues[] = request.getParameterValues(name);

        if (paramValues == null || paramValues.length == 0) {
            return new String[0];
        }

        List<String> values = new ArrayList<>(paramValues.length);
        for (int i = 0; i < paramValues.length; i++) {
            if (paramValues[i] != null && !"".equals(paramValues[i])) {
                values.add(paramValues[i]);
            }
        }

        return (String[]) values.toArray(new String[0]);
    }

    public static boolean getBooleanParameter(HttpServletRequest request, String name) {
        return getBooleanParameter(request, name, false);
    }

    public static boolean getBooleanParameter(HttpServletRequest request, String name,
                                              boolean defaultVal) {
        String temp = request.getParameter(name);
        if ("true".equalsIgnoreCase(temp) || "on".equalsIgnoreCase(temp)
            || "T".equalsIgnoreCase(temp) || "1".equalsIgnoreCase(temp)) {
            return true;
        }

        if ("false".equalsIgnoreCase(temp) || "off".equalsIgnoreCase(temp)
            || "F".equalsIgnoreCase(temp) || "0".equalsIgnoreCase(temp)) {
            return false;

        } else {
            return defaultVal;
        }
    }

    public static int getIntParameter(HttpServletRequest request, String name, int defaultNum) {
        String temp = request.getParameter(name);
        if (temp != null && !temp.equals("")) {
            int num = defaultNum;
            try {
                num = Integer.parseInt(temp.trim());

            } catch (Exception ignored) {

            }
            return num;
        } else {
            return defaultNum;
        }
    }

    public static int[] getIntParameters(HttpServletRequest request, String name, int defaultNum) {
        String paramValues[] = request.getParameterValues(name);

        if (paramValues == null || paramValues.length == 0) {
            return new int[0];
        }

        int values[] = new int[paramValues.length];

        for (int i = 0; i < paramValues.length; i++) {
            try {
                values[i] = Integer.parseInt(paramValues[i].trim());
            } catch (Exception e) {
                values[i] = defaultNum;
            }
        }

        return values;
    }

    public static float getFloatParameter(HttpServletRequest request, String name, float defaultNum) {
        String temp = request.getParameter(name);
        if (temp != null && !temp.equals("")) {
            float num = defaultNum;
            try {
                num = Float.parseFloat(temp.trim());

            } catch (Exception ignored) {

            }
            return num;
        } else {
            return defaultNum;
        }
    }
    
    public static float[] getFloatParameters(HttpServletRequest request, String name,
                                             float defaultNum) {
        String paramValues[] = request.getParameterValues(name);

        if (paramValues == null || paramValues.length == 0) {
            return new float[0];
        }

        float values[] = new float[paramValues.length];
        for (int i = 0; i < paramValues.length; i++) {

            try {
                values[i] = Float.parseFloat(paramValues[i].trim());

            } catch (Exception e) {
                values[i] = defaultNum;
            }
        }

        return values;
    }
    
    public static double getDoubleParameter(HttpServletRequest request, String name,
                                            double defaultNum) {
        String temp = request.getParameter(name);
        if (temp != null && !temp.equals("")) {
            double num = defaultNum;
            try {
                num = Double.parseDouble(temp.trim());

            } catch (Exception ignored) {

            }
            return num;
        } else {
            return defaultNum;
        }
    }

    

    public static double[] getDoubleParameters(HttpServletRequest request, String name,
                                               double defaultNum) {
        String paramValues[] = getParameters(request, name);

        if (paramValues == null || paramValues.length == 0) {
            return new double[0];
        }

        double values[] = new double[paramValues.length];
        for (int i = 0; i < paramValues.length; i++) {

            try {
                values[i] = Double.parseDouble(paramValues[i].trim());

            } catch (Exception e) {
                values[i] = defaultNum;
            }
        }

        return values;
    }

    public static long getLongParameter(HttpServletRequest request, String name, long defaultNum) {
        String temp = request.getParameter(name);
        if (temp != null && !temp.equals("")) {
            long num = defaultNum;
            try {

                num = Long.parseLong(temp.trim());
            } catch (Exception ignored) {

            }
            return num;
        } else {
            return defaultNum;
        }
    }

    public static long[] getLongParameters(HttpServletRequest request, String name, long defaultNum) {
        String paramValues[] = request.getParameterValues(name);
        if (paramValues == null || paramValues.length == 0) {
            return new long[0];
        }
        long values[] = new long[paramValues.length];
        for (int i = 0; i < paramValues.length; i++) {
            try {
                values[i] = Long.parseLong(paramValues[i].trim());

            } catch (Exception e) {
                values[i] = defaultNum;
            }

        }

        return values;
    }

    public static String getAttribute(HttpServletRequest request, String name) {
        return getAttribute(request, name, false);
    }

    public static String getAttribute(HttpServletRequest request, String name,
                                      boolean emptyStringsOK) {
        String temp = (String) request.getAttribute(name);
        if (temp != null) {
            if (temp.equals("") && !emptyStringsOK) {

                return null;
            } else {

                return temp;
            }
        } else {
            return null;
        }
    }

    public static boolean getBooleanAttribute(HttpServletRequest request, String name) {
        String temp = (String) request.getAttribute(name);
        return temp != null && temp.equals("true");
    }

    public static int getIntAttribute(HttpServletRequest request, String name, int defaultNum) {
        String temp = (String) request.getAttribute(name);
        if (temp != null && !temp.equals("")) {
            int num = defaultNum;
            try {
                num = Integer.parseInt(temp.trim());

            } catch (Exception ignored) {
            }
            return num;
        } else {
            return defaultNum;
        }
    }

    public static long getLongAttribute(HttpServletRequest request, String name, long defaultNum) {
        String temp = (String) request.getAttribute(name);
        if (temp != null && !temp.equals("")) {
            long num = defaultNum;
            try {
                num = Long.parseLong(temp.trim());

            } catch (Exception ignored) {

            }
            return num;
        } else {
            return defaultNum;
        }
    }

    public static Date getDateParameter(HttpServletRequest request, String name, Date defaultDate) {
        Date temp = getDateParameter(request, name);
        if (temp == null) {
            temp = defaultDate;
        }
        return temp;
    }

    public static Date getDateParameter(HttpServletRequest request, String name) {
        String temp = request.getParameter(name);
        if (temp == null) {
            return null;
        }
        try {
            if (temp.length() == 10) {
                return DateUtil.parse(temp, DateUtil.WEB_FORMAT);
            } else if (temp.length() > 10) {
                return DateUtil.parse(temp, DateUtil.NEW_FORMAT);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }
    
	
	public static final long DEFAULT_DATABASE_MIN_ID = 1;
	/**
	 * 身份证号验证
	 * @param idCard
	 * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isIdCard(String idCard){
		return IdCardVerification.isIdcard(idCard);
	}
	/**
	 * 合法图片验证
	 * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isLegalImg(String img){
		String regex = "^.*?\\.(jpg|png|gif)$";
        return img == null || "".equals(img.trim()) ? false : Pattern.matches(   
        		regex, img);  
	}
	public static boolean isLegalImgList(List<String> imgs){
		if(null == imgs || imgs.size() == 0)
			return false;
		
		for(String img : imgs){
			if(!isLegalImg(img))
				return false;
		}
		return true;
	}
	/**
	* 验证邮箱
	* 
	* @param 待验证的字符串
	* @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	*/
	public static boolean isEmail(String email) {
		if(StrUtil.isBlank(email)){
			return false;
		}
		String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		return Pattern.matches(regex, email);
	}
	
	public static boolean isPhone(String phone){
		if(StrUtil.isBlank(phone)){
			return false;
		}
	    String regex = "^1[3|4|5|7|8]\\d{9}$";
		String regex1 = "^0\\d{2,3}-?\\d{7,8}$";
	    return Pattern.matches(regex, phone) || Pattern.matches(regex1, phone);
	}
	
	public static boolean isQQ(String qq){
		if(StrUtil.isBlank(qq)){
			return false;
		}
		String regex = "^[1-9][0-9]{4,9}$";
		return Pattern.matches(regex, qq);
	}
	
	public static boolean isOnlyEnglishAndNumber(String word){
		if(StrUtil.isBlank(word)){
			return false;
		}
		String regex = "^[A-Za-z0-9]+$";
		return Pattern.matches(regex, word);
	}
	
	public static boolean isNull(Object object){
		return null == object;
	}
	
	public static boolean isNotNull(Object object){
		return !isNull(object);
	}
	
	public static <E> boolean isEmpty(Collection<E> collection){
		return null == collection || collection.size() == 0;
	}
	
	public static <E>  boolean isNotEmpty(Collection<E> collection){
		return !isEmpty(collection);
	}
	public static <K,V> boolean isEmpty(Map<K,V> collection){
		return null == collection || collection.size() == 0;
	}
	
	public static <K,V> boolean isNotEmpty(Map<K,V> collection){
		return !isEmpty(collection);
	}
}
