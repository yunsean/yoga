package com.yoga.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeCastUtil {
	
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATE_FORMAT_EXACT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
	public final static String TIME_FORMAT_EXACT = "HH:mm:ss";
	public final static String TIME_FORMAT_HM = "HH:mm";
	
	public static long toLong(Object object){
       if(null == object)return 0;
	   if (object instanceof Integer) {
		   	int i = ((Integer) object).intValue();
		   	return i;
	   } else if (object instanceof String) {
		   long l = 0;
		   try{
			   l = Long.parseLong((String) object);
		   }catch(Exception e){
			   
		   }
		   return l;
	   } else if (object instanceof Double) {
		   double d = ((Double) object).doubleValue();
	    	return (long)d;
	   } else if (object instanceof Float) {
		   float f = ((Float) object).floatValue();
	    	return (long)f;
	   } else if (object instanceof Long) {
		    long l = ((Long) object).longValue();
		    return l;
	   } else if (object instanceof Boolean) {
		    boolean b = ((Boolean) object).booleanValue();
		    return b?1:0;
	   } else if (object instanceof Date) {
		   Date d = (Date) object;
		   return d.getTime();
	   }
		
		return 0;
	}	

	public static float toFloat(Object object){
       if(null == object)return 0;
	   if (object instanceof Integer) {
		   	int i = ((Integer) object).intValue();
		   	return i;
	   } else if (object instanceof String) {
		   float l = 0;
		   try{
			   l = Float.parseFloat((String) object);
		   }catch(Exception e){
			   
		   }
		   return l;
	   } else if (object instanceof Double) {
		   double d = ((Double) object).doubleValue();
	    	return (float)d;
	   } else if (object instanceof Float) {
		   float f = ((Float) object).floatValue();
	    	return f;
	   } else if (object instanceof Long) {
		    float l = ((Long) object).floatValue();
		    return l;
	   } else if (object instanceof Boolean) {
		    boolean b = ((Boolean) object).booleanValue();
		    return b?1:0;
	   } else if (object instanceof Date) {
		   Date d = (Date) object;
		   return d.getTime();
	   }
		
		return 0;
	}
	
	public static double toDouble(Object object){
	       if(null == object)return 0;
		   if (object instanceof Integer) {
			   	int i = ((Integer) object).intValue();
			   	return i;
		   } else if (object instanceof String) {
			   double l = 0;
			   try{
				   l = Double.parseDouble((String) object);
			   }catch(Exception e){
				   
			   }
			   return l;
		   } else if (object instanceof Double) {
			   double d = ((Double) object).doubleValue();
		    	return d;
		   } else if (object instanceof Float) {
			   double f = ((Float) object).doubleValue();
			   return f;
		   } else if (object instanceof Long) {
			    double l = ((Long) object).doubleValue();
			    return l;
		   } else if (object instanceof Boolean) {
			    boolean b = ((Boolean) object).booleanValue();
			    return b?1:0;
		   } else if (object instanceof Date) {
			   Date d = (Date) object;
			   return d.getTime();
		   }
		   return 0;
		}
	
	public static String toString(Object object){
		   if(null == object)return null;
		   if (object instanceof Integer) {
			   	int i = ((Integer) object).intValue();
				return ""+i;
		   } else if (object instanceof String) {
			   	String s = (String) object;
			   	return s;
		   } else if (object instanceof Double) {
			   	double d = ((Double) object).doubleValue();
		    	return ""+d;
		   } else if (object instanceof Float) {
			   	float f = ((Float) object).floatValue();
		    	return ""+f;
		   } else if (object instanceof Long) {
			    long l = ((Long) object).longValue();
			    return ""+l;
		   } else if (object instanceof Boolean) {
			    boolean b = ((Boolean) object).booleanValue();
			    return b?"true":"false";
		   } else if (object instanceof Date) {
			   	Date d = (Date) object;
			   	return ""+d;
		   }
		
		return "";
	}
	
	public static int toInt(Object object){
		   if(null == object)return 0;
		   if (object instanceof Integer) {
			   	int i = ((Integer) object).intValue();
			   	return i;
		   } else if (object instanceof String) {
			   	int s = 0;
			   	try{
			   		s = Integer.parseInt((String)object);
			   	}catch(Exception e){
			   		
			   	}
			   	return s;
		   } else if (object instanceof Double) {
			   double d = ((Double) object).doubleValue();
		    	return (int)d;
		   } else if (object instanceof Float) {
			   float f = ((Float) object).floatValue();
		    	return (int)f;
		   } else if (object instanceof Long) {
			    long l = ((Long) object).longValue();
			    return (int)l;
		   } else if (object instanceof Boolean) {
			    boolean b = ((Boolean) object).booleanValue();
			    return b?1:0;
		   } else if (object instanceof Date) {
			   return 0;
		   }
		   return 0;
	}
	
	public static Date toDate(Object object){
		if(null == object) return null;
		if(object instanceof Date){
			return (Date)object;
		}else if(object instanceof String){
			String temp = (String) object;			
			try{
				if (temp.matches("[0-9]+")) {
					return new Date(Long.parseLong(temp));
				} else if(temp.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
					return new SimpleDateFormat(DATE_FORMAT).parse(temp);
				} else if(temp.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")){
					return new SimpleDateFormat(DATE_FORMAT_EXACT).parse(temp);
				} else if(temp.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}")){
					return new SimpleDateFormat(DATE_FORMAT_YMDHM).parse(temp);
				} else if(temp.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}")){
					return new SimpleDateFormat(TIME_FORMAT_EXACT).parse(temp);
				} else if(temp.matches("[0-9]{2}:[0-9]{2}")){
					return new SimpleDateFormat(TIME_FORMAT_HM).parse(temp);
				}
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
}
