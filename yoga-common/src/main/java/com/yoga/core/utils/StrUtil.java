package com.yoga.core.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StrUtil {
	public static String map2Query(Map<String, Object> query) {
		StringBuffer buffer = new StringBuffer(1024);
		for (Map.Entry<String, Object> entry : query.entrySet()) {  
			buffer.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue().toString()) + "&"); 
		}
		if (buffer.length() > 0) {
			buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();
	}

	public static String lowerCase(String src) {
		if (src == null) return null;
		return src.toLowerCase();
	}
	
	public static String guid() {
		return UUID.randomUUID().toString();
	}

	public static String list2String(Collection<String> items) {
		if (items == null) return null;
		StringBuilder sb = new StringBuilder();
		for (String item: items) {
			sb.append("*").append(item);
		}
		if (sb.length() > 0) sb.deleteCharAt(0);
		return sb.toString();
	}
	public static String array2String(String[] items) {
		if (items == null) return null;
		StringBuilder sb = new StringBuilder();
		for (String item: items) {
			sb.append("*").append(item);
		}
		if (sb.length() > 0) sb.deleteCharAt(0);
		return sb.toString();
	}
	public static String[] string2Array(String sequence) {
		if (sequence == null) return null;
		return sequence.split("\\*");
	}
	public static List<String> string2List(String sequence) {
		if (sequence == null) return null;
		return Arrays.stream(sequence.split("\\*")).collect(Collectors.toList());
	}

	public static boolean isEqual(String left, String right) {
		if (left == null && right == null) return true;
		if (left == null && right != null) return false;
		return left.equals(right);
	}
	public static boolean isEqualIgnoreCase(String left, String right) {
		if (left == null && right == null) return true;
		if (left == null && right != null) return false;
		return left.equalsIgnoreCase(right);
	}
	public static boolean isContain(String text, String filter) {
		if (text == null) return false;
		return text.contains(filter);
	}

	public static boolean isEmpty(String str){
		return null == str || str.length() == 0;
	}
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
        return null == str || "".equals(str.trim());
    }
	public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
	public static boolean hasBlank(String... strs) {
        if (null == strs || 0 == strs.length) {
            return true;
        } else {
            for (String str : strs) {
                if (isBlank(str)) {
                    return true;
                }
            }
        }
        return false;
    }
	public static boolean hasNotBlank(String... strs) {
		if (null == strs || 0 == strs.length) {
			return false;
		} else {
			for (String str : strs) {
				if (isNotBlank(str)) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean allBlank(String... strs) {
		if (null == strs || 0 == strs.length) {
			return true;
		} else {
			for (String str : strs) {
				if (isNotBlank(str)) {
					return false;
				}
			}
		}
		return true;
	}
	public static boolean allNotBlank(String... strs) {
		if (null == strs || 0 == strs.length) {
			return false;
		} else {
			for (String str : strs) {
				if (isBlank(str)) {
					return false;
				}
			}
		}
		return true;
	}

	public static String formatDate(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
		String dateString = formatter.format(date);
		return dateString;
	}
	public static String formatDateLong(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		String dateString = formatter.format(date);
		return dateString;
	}
	public static String formatDateShort(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		String dateString = formatter.format(date);
		return dateString;
	}
	public static String formatTimeShort(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
		String dateString = formatter.format(date);
		return dateString;
	}
	public static Date toDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
		try {
			Date strtodate = formatter.parse(date);
			return strtodate;			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Date();
	}
	public static Date toDateLong(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		try {
			Date strtodate = formatter.parse(date);
			return strtodate;			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Date();
	}
	public static Date toDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try {
			Date strtodate = formatter.parse(date);
			return strtodate;			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Date();
	}
	public static Date toTime(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
		try {
			Date strtodate = formatter.parse(date);
			return strtodate;			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Date();
	}

	public static boolean isInt(String value) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(value).matches();
	}

	public static int toInt(String value) {
		return toInt(value, 0);
	}
	public static int toInt(String value, int def) {
		try {
			return Integer.valueOf(value);
		} catch (Exception ex) {
			ex.printStackTrace();
			return def;
		}
	}

	public static float toFloat(String value) {
		return toFloat(value, 0);
	}
	public static float toFloat(String value, float def) {
		try {
			return Float.valueOf(value);
		} catch (Exception ex) {
			ex.printStackTrace();
			return def;
		}
	}
	
	public static double doDouble(String value) {
		return toFloat(value, 0);
	}
	public static double doDouble(String value, double def) {
		try {
			return Double.valueOf(value);
		} catch (Exception ex) {
			ex.printStackTrace();
			return def;
		}
	}
	
	public static String phoneNoHide(String phone) {
        // 括号表示组，被替换的部分$n表示第n组的内容
        // 正则表达式中，替换字符串，括号的意思是分组，在replace()方法中，
        // 参数二中可以使用$n(n为数字)来依次引用模式串中用括号定义的字串。
        // "(\d{3})\d{4}(\d{4})", "$1****$2"的这个意思就是用括号，
        // 分为(前3个数字)中间4个数字(最后4个数字)替换为(第一组数值，保持不变$1)(中间为*)(第二组数值，保持不变$2)
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
    //银行卡号，保留最后4位，其他星号替换
    public static String cardIdHide(String cardId) {
        return cardId.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1");
    }
    //是否为车牌号（沪A88888）
    public static boolean isVehicleNo(String vehicleNo) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$");
        return pattern.matcher(vehicleNo).find();
    }
    public static boolean isPdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }
    //验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
    public static boolean isMobileNo(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }
    public static boolean isPhoneNo(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }
    public static boolean isEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }
    //验证整数（正整数和负整数）
    public static boolean isDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }
    //验证整数和浮点数（正负整数和正负浮点数）
    public static boolean isDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }
    // 验证空白字符
    public static boolean isBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }
    //验证中文
    public static boolean isChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }
    //验证日期（年月日）格式：1992-09-03，或1992.09.03
    public static boolean isBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }
    //验证URL地址 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
    public static boolean isURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }
    // 匹配中国邮政编码
    public static boolean isPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }
    // 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
    public static boolean isIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
    //base64加密
    public static String base64Encoder( byte[] bytes){
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bytes);
	}
	//base解密
	public static byte[] base64Decode (String str) {
		byte[] data=null;
    	try {
			BASE64Decoder decoder = new BASE64Decoder();
			data= decoder.decodeBuffer(str);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return data;
	}
    public static String alignRight(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }

        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;

        if (pads <= 0) {
            return str;
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();

            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }

            return new String(padding).concat(str);
        }
    }
}
