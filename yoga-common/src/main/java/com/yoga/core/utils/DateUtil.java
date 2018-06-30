package com.yoga.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具类
 * @author Skysea
 *
 */
public class DateUtil {
	public final static String WEB_FORMAT = "yyyy-MM-dd";
	public final static String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static long getTodayBeginTimes(){
		return getTodayBeginDate().getTime();
	}
	
	public static Date getTodayBeginDate(){
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}
	public static Date parse(String date, String format) throws ParseException {
		if (date == null) {
			return null;
		}

		return new SimpleDateFormat(format).parse(date);
	}

	public static Date beginOfDay(Date date) {
		Calendar now = Calendar.getInstance();
		long offset = now.get(Calendar.DST_OFFSET) - now.get(Calendar.ZONE_OFFSET);
		long millisecond = date.getTime();
		millisecond -= offset;
		millisecond -= millisecond % (3600 * 24 * 1000);
		millisecond += offset;
		return new Date(millisecond);
	}
	public static Date endOfDay(Date date) {
		Calendar now = Calendar.getInstance();
		long offset = now.get(Calendar.DST_OFFSET) - now.get(Calendar.ZONE_OFFSET);
		long millisecond = date.getTime();
		millisecond -= offset;
		millisecond += (3600 * 24 * 1000 - 1) - (millisecond % (3600 * 24 * 1000));
		millisecond += offset;
		return new Date(millisecond);
	}
	public static Date dateOffset(Date date, float hour) {
		long millisecond = date.getTime();
		millisecond += (long)(hour * (3600 * 1000));
		return new Date(millisecond);
	}
	public static float offsetHour(Date end, Date begin) {
		long beginMs = begin.getTime();
		long endMs = end.getTime();
		return (endMs - beginMs) / (3600 * 1000);
	}
	public static Calendar beginOfDay(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.clear();
		result.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return result;
	}
	public static Calendar endOfDay(Calendar cal) {
		Calendar result = Calendar.getInstance();
		result.clear();
		result.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return result;
	}
	public static Calendar adjustTime(Calendar date, int hour, int minute, int second) {
		if (date == null)return null;
		date.set(Calendar.HOUR_OF_DAY, hour);
		date.set(Calendar.MINUTE, minute);
		date.set(Calendar.SECOND, second);
		date.set(Calendar.MILLISECOND, 0);
		date.getTimeInMillis();
		return date;
	}

	public static Date fromUTC(long utc) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(utc);
		return calendar.getTime();
	}
	public static String formatDate(long utc, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(utc);
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}
	public static String formatDateLong(long utc) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(utc);
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}
	public static String formatDateShort(long utc) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(utc);
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}
	public static String formatTimeShort(long utc) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(utc);
		String dateString = formatter.format(calendar.getTime());
		return dateString;
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

	public static int daysBetween(Calendar begin, Calendar end)   {
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		begin.set(Calendar.MILLISECOND, 0);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MILLISECOND, 0);
		long time2 = end.getTimeInMillis();
		long time1 = begin.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24) + 1;
		return (int)between_days;
	}
	public  String twoDateDistance(Date startDate, Date endDate){
		if(startDate == null ||endDate == null){
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 60 * 1000) {
			return timeLong / 1000 + "秒前";
		}
		else if (timeLong < 60 * 60 * 1000){
			timeLong = timeLong / 1000 / 60;
			return timeLong + "分钟前";
		} else if (timeLong < 60 * 60 * 24 * 1000){
			timeLong = timeLong / 60 / 60 / 1000;
			return timeLong + "小时前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7){
			timeLong = timeLong / 1000/ 60 / 60 / 24;
			return timeLong + "天前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4){
			timeLong = timeLong/1000/ 60 / 60 / 24 / 7;
			return timeLong + "周前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(startDate);
		}
	}

	public static boolean isSameDay(Calendar left, Calendar right) {
		if (left.get(Calendar.YEAR) != right.get(Calendar.YEAR)) return false;
		else if (left.get(Calendar.DAY_OF_YEAR) != right.get(Calendar.DAY_OF_YEAR)) return false;
		else return true;
	}

	public static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthday)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	public static long timeZoneOffset() {
		return TimeZone.getDefault().getRawOffset();
	}
	public static long timeInMillis(Calendar calendar) {
		long timeInMillis = calendar.getTimeInMillis();
		return timeInMillis + calendar.get(Calendar.ZONE_OFFSET);
	}
	public static long timeInMillis(Date date) {
		long timeInMillis = date.getTime();
		return timeInMillis + date.getTimezoneOffset();
	}
}
