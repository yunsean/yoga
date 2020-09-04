package com.yoga.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 
 * @author qu.raoq
 * @version $Id: DateUtils.java, v 0.1 2016年10月2日 下午6:40:05 qu.raoq Exp $
 */
public class DateUtils {

    // 完整时间
    public static final String   simple            = "yyyy-MM-dd HH:mm:ss";

    // private static final DateFormat simple = new SimpleDateFormat();
    // 年月日
    public static final String   dtSimple          = "yyyy-MM-dd";

    // private static final DateFormat dtSimple = new
    // SimpleDateFormat("yyyy-MM-dd");
    // 年月日
    public static final String   dtSimpleChinese   = "yyyy年MM月dd日";

    // private static final DateFormat dtSimpleChinese = new
    // SimpleDateFormat("yyyy年MM月dd日");
    // 年月日(无下划线)
    public static final String   dtShort           = "yyyyMMdd";

    // 年月日时分秒(无下划线)
    public static final String   dtLong            = "yyyyMMddHHmmss";

    // private static final DateFormat dtShort = new
    // SimpleDateFormat("yyyyMMdd");
    // 时分秒
    public static final String   hmsFormat         = "HH:mm:ss";

    // private static final DateFormat hmsFormat = new
    // SimpleDateFormat("HH:mm:ss");
    public static final String   simpleFormat      = "yyyy-MM-dd HH:mm";

    // 年月日时分秒毫秒(无下划线)
    public static final String   dtLongMill        = "yyyyMMddHHmmssS";
    /**
     * The UTC time zone  (often referred to as GMT).
     */
    public static final TimeZone UTC_TIME_ZONE     = TimeZone.getTimeZone("GMT");
    /**
     * Number of milliseconds in a standard second.
     * @since 2.1
     */
    public static final long     MILLIS_PER_SECOND = 1000;
    /**
     * Number of milliseconds in a standard minute.
     * @since 2.1
     */
    public static final long     MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in a standard hour.
     * @since 2.1
     */
    public static final long     MILLIS_PER_HOUR   = 60 * MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in a standard day.
     * @since 2.1
     */
    public static final long     MILLIS_PER_DAY    = 24 * MILLIS_PER_HOUR;

    // private static final DateFormat simpleFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final DateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * 
     * @return
     */
    public static final String simpleFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(simple).format(date);
    }

    /**
     * yyyy-MM-dd
     * 
     * @param date
     * 
     * @return
     */
    public static final String dtSimpleFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtSimple).format(date);
    }

    /**
     * yyyyMMddHHmmssS
     * 
     * @param date
     * 
     * @return
     */
    public static final String dtLongMillFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtLongMill).format(date);
    }

    /**
     * yyyy-mm-dd 日期格式转换为日期
     * 
     * @param strDate
     * 
     * @return
     */
    public static final Date strToDtSimpleFormat(String strDate) {
        if (strDate == null) {
            return null;
        }

        try {
            return getFormat(dtSimple).parse(strDate);
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * yyyy-MM-dd HH:mm 日期格式转换为日期
     * 
     * @param strDate
     * 
     * @return
     */
    public static final Date strToSimpleFormat(String strDate) {
        if (strDate == null) {
            return null;
        }

        try {
            return getFormat(simpleFormat).parse(strDate);

        } catch (Exception e) {
        }

        return null;
    }

    /**
     * yyyy-MM-dd HH:mm 或者yyyy-MM-dd  转换为日期格式
     * @param strDate
     * @return
     */
    public static final Date strToDate(String strDate) {
        if (strToSimpleFormat(strDate) != null) {
            return strToSimpleFormat(strDate);
        } else {
            return strToDtSimpleFormat(strDate);
        }

    }

    /**
     * 获取输入日期的相差日期
     * 
     * @param dt
     * @param idiff
     * 
     * @return
     */
    public static final String getDiffDate(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();

        c.setTime(dt);
        c.add(Calendar.DATE, idiff);
        return dtSimpleFormat(c.getTime());
    }

    /**
     * 获取输入日期月份的相差日期
     * 
     * @param dt
     * @param idiff
     * @return
     */
    public static final String getDiffMon(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, idiff);
        return dtSimpleFormat(c.getTime());
    }

    /**
     * yyyy年MM月dd日
     * 
     * @param date
     * 
     * @return
     */
    public static final String dtSimpleChineseFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtSimpleChinese).format(date);
    }

    /**
     * yyyy-MM-dd到 yyyy年MM月dd日 转换
     * 
     * @param date
     * 
     * @return
     */
    public static final String dtSimpleChineseFormatStr(String date) throws ParseException {
        if (date == null) {
            return "";
        }

        return getFormat(dtSimpleChinese).format(string2Date(date));
    }

    /**
     * yyyy-MM-dd 日期字符转换为时间
     * 
     * @param stringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final Date string2Date(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }

        return getFormat(dtSimple).parse(stringDate);
    }

    /**
     * 返回日期时间（Add by Sunzy）
     * 
     * @param stringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final Date string2DateTime(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }

        return getFormat(simple).parse(stringDate);
    }

    /**
     * 返回日期时间（Add by Sunzy）
     * 
     * @param stringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final Date string2DateTimeByAutoZero(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        if (stringDate.length() == 11)
            stringDate = stringDate + "00:00:00";
        else if (stringDate.length() == 13)
            stringDate = stringDate + ":00:00";
        else if (stringDate.length() == 16)
            stringDate = stringDate + ":00";
        else if (stringDate.length() == 10)
            stringDate = stringDate + " 00:00:00";

        return getFormat(simple).parse(stringDate);
    }

    /**
     * 计算日期差值
     * 
     * @param String
     * @param String
     * @return int（天数）
     */
    public static final int calculateDecreaseDate(String beforDate,
                                                  String afterDate) throws ParseException {
        Date date1 = getFormat(dtSimple).parse(beforDate);
        Date date2 = getFormat(dtSimple).parse(afterDate);
        long decrease = getDateBetween(date1, date2) / 1000 / 3600 / 24;
        int dateDiff = (int) decrease;
        return dateDiff;
    }

    /**
     * 计算时间差
     * 
     * @param dBefor
     *            首日
     * @param dAfter
     *            尾日
     * @return 时间差(毫秒)
     */
    public static final long getDateBetween(Date dBefor, Date dAfter) {
        long lBefor = 0;
        long lAfter = 0;
        long lRtn = 0;

        /** 取得距离 1970年1月1日 00:00:00 GMT 的毫秒数 */
        lBefor = dBefor.getTime();
        lAfter = dAfter.getTime();

        lRtn = lAfter - lBefor;

        return lRtn;
    }

    /**
     * 返回日期时间（Add by Gonglei）
     * 
     * @param stringDate
     *            (yyyyMMdd)
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final Date shortstring2Date(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }

        return getFormat(dtShort).parse(stringDate);
    }

    /**
     * 返回短日期格式（yyyyMMdd格式）
     * 
     * @param stringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final String shortDate(Date Date) {
        if (Date == null) {
            return null;
        }

        return getFormat(dtShort).format(Date);
    }

    /**
     * 返回长日期格式（yyyyMMddHHmmss格式）
     * 
     * @param stringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final String longDate(Date Date) {
        if (Date == null) {
            return null;
        }

        return getFormat(dtLong).format(Date);
    }

    /**
     * yyyy-MM-dd 日期字符转换为长整形
     * 
     * @param stringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final Long string2DateLong(String stringDate) throws ParseException {
        Date d = string2Date(stringDate);

        if (d == null) {
            return null;
        }

        return new Long(d.getTime());
    }

    /**
     * 日期转换为字符串 HH:mm:ss
     * 
     * @param date
     * 
     * @return
     */
    public static final String hmsFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(hmsFormat).format(date);
    }

    /**
     * 时间转换字符串 2005-06-30 15:50
     * 
     * @param date
     * 
     * @return
     */
    public static final String simpleDate(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(simpleFormat).format(date);
    }

    /**
     * 获取当前日期的日期差 now= 2005-07-19 diff = 1 -> 2005-07-20 diff = -1 -> 2005-07-18
     * 
     * @param diff
     * 
     * @return
     */
    public static final String getDiffDate(int diff) {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        return dtSimpleFormat(c.getTime());
    }

    public static final Date getDiffDateTime(int diff) {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        return c.getTime();
    }

    /**
     * 获取当前日期的日期时间差
     * 
     * @param diff
     * @param hours
     * 
     * @return
     */
    public static final String getDiffDateTime(int diff, int hours) {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, diff);
        c.add(Calendar.HOUR, hours);
        return dtSimpleFormat(c.getTime());
    }

    public static final String getDiffDate(String srcDate, String format, int diff) {
        DateFormat f = new SimpleDateFormat(format);

        try {
            Date source = f.parse(srcDate);
            Calendar c = Calendar.getInstance();

            c.setTime(source);
            c.add(Calendar.DATE, diff);
            return f.format(c.getTime());
        } catch (Exception e) {
            return srcDate;
        }
    }

    /**
     * 把日期类型的日期换成数字类型 YYYYMMDD类型
     * 
     * @param date
     * 
     * @return
     */
    public static final Long dateToNumber(Date date) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();

        c.setTime(date);

        String month;
        String day;

        if ((c.get(Calendar.MONTH) + 1) >= 10) {
            month = "" + (c.get(Calendar.MONTH) + 1);
        } else {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        if (c.get(Calendar.DATE) >= 10) {
            day = "" + c.get(Calendar.DATE);
        } else {
            day = "0" + c.get(Calendar.DATE);
        }

        String number = c.get(Calendar.YEAR) + "" + month + day;

        return new Long(number);
    }

    /**
     * 获取每月的某天到月末的区间
     * 
     * @param date
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map getLastWeek(String StringDate, int interval) throws ParseException {
        Map lastWeek = new HashMap();
        Date tempDate = DateUtils.shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);

        int dayOfMonth = cad.getActualMaximum(Calendar.DAY_OF_MONTH);

        cad.add(Calendar.DATE, (dayOfMonth - 1));
        lastWeek.put("endDate", DateUtils.shortDate(cad.getTime()));
        cad.add(Calendar.DATE, interval);
        lastWeek.put("startDate", DateUtils.shortDate(cad.getTime()));

        return lastWeek;
    }

    /**
     * 获取下月
     * 
     * @param date
     * 
     * @return
     */
    public static String getNextMon(String StringDate) throws ParseException {
        Date tempDate = DateUtils.shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.MONTH, 1);
        return DateUtils.shortDate(cad.getTime());
    }

    /**
     * add by daizhixia 20050808 获取下一天
     * 
     * @param StringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static String getNextDay(String StringDate) throws ParseException {
        Date tempDate = DateUtils.string2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, 1);
        return DateUtils.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by chencg 获取下一天 返回 dtSimple 格式字符
     * 
     * @param date
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static String getNextDay(Date date) throws ParseException {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(Calendar.DATE, 1);
        return DateUtils.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by shengyong 20050808 获取前一天
     * 
     * @param StringDate
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static String getBeforeDay(String StringDate) throws ParseException {
        Date tempDate = DateUtils.string2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, -1);
        return DateUtils.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by shengyong 获取前一天 返回 dtSimple 格式字符
     * 
     * @param date
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static String getBeforeDay(Date date) throws ParseException {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(Calendar.DATE, -1);
        return DateUtils.dtSimpleFormat(cad.getTime());
    }

    /**
     * add by chencg 获取下一天 返回 dtshort 格式字符
     * 
     * @param StringDate
     *            "20061106"
     * 
     * @return String "2006-11-07"
     * 
     * @throws ParseException
     */
    public static Date getNextDayDtShort(String StringDate) throws ParseException {
        Date tempDate = DateUtils.shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(Calendar.DATE, 1);
        return cad.getTime();
    }

    /**
     * add by daizhixia 20050808 取得相差的天数
     * 
     * @param startDate
     * @param endDate
     * 
     * @return
     */
    public static long countDays(String startDate, String endDate) {
        Date tempDate1 = null;
        Date tempDate2 = null;
        long days = 0;

        try {
            tempDate1 = DateUtils.string2Date(startDate);

            tempDate2 = DateUtils.string2Date(endDate);
            days = (tempDate2.getTime() - tempDate1.getTime()) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 返回日期相差天数，向下取整数
     * 
     * @param dateStart
     *            一般前者小于后者dateEnd
     * @param dateEnd
     * 
     * @return
     */
    public static int countDays(Date dateStart, Date dateEnd) {
        if ((dateStart == null) || (dateEnd == null)) {
            return -1;
        }

        return (int) ((dateEnd.getTime() - dateStart.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 校验start与end相差的天数，是否满足end-start lessEqual than days
     * 
     * @param start
     * @param end
     * @param days
     * 
     * @return
     */
    public static boolean checkDays(Date start, Date end, int days) {
        int g = countDays(start, end);

        return g <= days;
    }

    public static Date now() {
        return new Date();
    }

    /**
     * alahan add 20050825 获取传入时间相差的日期
     * 
     * @param dt
     *            传入日期，可以为空
     * @param diff
     *            需要获取相隔diff天的日期 如果为正则取以后的日期，否则时间往前推
     * 
     * @return
     */
    public static String getDiffStringDate(Date dt, int diff) {
        Calendar ca = Calendar.getInstance();

        if (dt == null) {
            ca.setTime(new Date());
        } else {
            ca.setTime(dt);
        }

        ca.add(Calendar.DATE, diff);
        return dtSimpleFormat(ca.getTime());
    }

    /**
     * 校验输入的时间格式是否合法，但不需要校验时间一定要是8位的
     * 
     * @param statTime
     * 
     * @return alahan add 20050901
     */
    public static boolean checkTime(String statTime) {
        if (statTime.length() > 8) {
            return false;
        }

        String[] timeArray = statTime.split(":");

        if (timeArray.length != 3) {
            return false;
        }

        for (int i = 0; i < timeArray.length; i++) {
            String tmpStr = timeArray[i];

            try {
                Integer tmpInt = new Integer(tmpStr);

                if (i == 0) {
                    if ((tmpInt.intValue() > 23) || (tmpInt.intValue() < 0)) {
                        return false;
                    } else {
                        continue;
                    }
                }

                if ((tmpInt.intValue() > 59) || (tmpInt.intValue() < 0)) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    /**
     * 返回日期时间（Add by Gonglei）
     * 
     * @param stringDate
     *            (yyyyMMdd)
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static final String StringToStringDate(String stringDate) {
        if (stringDate == null) {
            return null;
        }

        if (stringDate.length() != 8) {
            return null;
        }

        return stringDate.substring(0, 4) + stringDate.substring(4, 6) + stringDate.substring(6, 8);
    }

    /**
     * 将字符串按format格式转换为date类型
     * 
     * @param str
     * @param format
     * 
     * @return
     */
    public static Date string2Date(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 加减天数
     * 
     * @param date
     * @return Date
     * @author shencb 2006-12 add
     */
    public static final Date increaseDate(Date aDate, int days) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(aDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 把日期2007/06/14转换为20070614
     * @author Yufeng 2007
     * @method formatDateString
     * @param date
     * @return
     */
    public static String formatDateString(String date) {
        String result = "";
        if (StringUtil.isBlank(date)) {
            return "";
        }
        if (date.length() == 10) {
            result = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
        }
        return result;
    }

    /**
     * 获得日期是周几
     * @author xiang.zhaox
     * @param date
     * @return dayOfWeek
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static void main1(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(getFormat(dtLongMill).format(new Date()));
        }

    }

    /**
     * 将8位日期转换为10位日期（Add by Alcor）
     * 
     * @param shortString yyyymmdd
     * @return yyyy-mm-dd
     * @throws ParseException
     */
    public static final String shortString2SimpleString(String shortString) {
        if (shortString == null) {
            return null;
        }
        try {
            return getFormat(dtSimple).format(shortstring2Date(shortString));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间分段
     * @param day 时间天数
     * @param size 分段粒度 1 2 3 4
     * @param date 默认当前时间
     * @return
     */
    public static List<Map<String,Object>> segmentedDate(int day,int size,Date date){
        if(size == 1)size = day;
        int average = day / size;
        if(date == null){
            date = new Date(System.currentTimeMillis());
        }
        Date startDate = DateUtils.increaseDate(date,-day);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(int i=0; i<=size; i++){
            Map<String,Object> objectMap = new HashMap<>();
            Date endDate = DateUtils.increaseDate(startDate,average);
            objectMap.put("showTime",dtSimpleFormat(startDate));
            objectMap.put("beginDate",startDate.getTime() / 1000);
            objectMap.put("endDate",endDate.getTime() / 1000);
            objectMap.put("segmentedDate",DateUtils.dateToNumber(startDate)+"-"+DateUtils.dateToNumber(endDate));
            startDate = endDate;
            mapList.add(objectMap);
        }
        return mapList;
    }

    /**
     * 判断选择的日期是否是今天
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    /**
     * 判断选择的日期是否是本周
     * @param time
     * @return
     */
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    /**
     * 判断选择的日期是否是本月
     * @param time
     * @return
     */
    public static boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        System.out.println(param);
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    /**
     * 获得本月的开始时间
     *
     * @return
     */
    public static Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = getFormat(dtSimple).parse(getFormat(dtSimple).format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public static Date getCurrentWeekDayStartTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(getFormat(simple).parse(getFormat(dtSimple).format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }



    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public static Date getCurrentWeekDayEndTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(getFormat(simple).parse(getFormat(dtSimple).format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本天的开始时间
     *
     * @return
     */
    public static Date getCurrentDayStartTime() {
        Date now = new Date();
        try {
            now = getFormat(dtSimple).parse(getFormat(dtSimple).format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取某天的结束时间
     * @return
     */
    public static Date getCurrentDayEndTime(Long time) {
        if(time == null){
            time = System.currentTimeMillis();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal.getTime();
    }



}
