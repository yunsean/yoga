package com.yoga.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeIDOperator {

	private static String stringkey ="a1b2cd3e4f5ghi6jklm7nop8qrstu9vwx0yz";
	private static Integer[] numberKey = {1,86, 21, 62, 59,17};
	
	public static String idToCode(long id){
		String mfId = String.valueOf(id);
		if(mfId.length() > 10){
			return null;
		}
		List<Object> list = getKey();
		List<String> numString = (List<String>)list.get(0);
		Map<String,Integer> stringNum = (Map<String,Integer>)list.get(1);
		int i = 1;
		for(String c : numString){
			i++;
		}
		int idl = mfId.length()-1;
		int len = 11-idl;
		String stringId = idl+""+mfId+"";
		for(int j = 0;j<len;j++){
			stringId +=(int)(Math.random()*10);
		}
		String code ="";
		for(int j =0;j<6;j++){
			String a = String.valueOf(stringId.charAt(2*j));
			String b = String.valueOf(stringId.charAt(2*j+1));
			int num = (Integer.valueOf(a+b)+numberKey[j])%100;
			code +=numString.get(num);
		}
		return code;
	}
	public static Integer codeToId(String code){
		Integer id = null;
		if(code.length() == 12){
			List<Object> list = getKey();
			List<String> numString = (List<String>)list.get(0);
			Map<String,Integer> stringNum = (Map<String,Integer>)list.get(1);
			String stringId = "";
			for(int j = 0; j< 6;j++){
			String a = String.valueOf(code.charAt(2*j));
			String b = String.valueOf(code.charAt(2*j+1));
			Integer n = stringNum.get(a+b);
			n = n - numberKey[j];
			if(n < 0){
				n = n+100;
			}
			String ns = String.valueOf(n);
			if(ns.length() < 2){
				ns = "0"+ns;
			}
			stringId += ns;
			}
			id = Integer.valueOf(stringId.substring(1, Integer.parseInt(String.valueOf(stringId.charAt(0)))+2));
		}
		return id;
	}
	private static List<Object> getKey(){
		List<Object> list = new ArrayList<Object>();
		List<String> numString = new ArrayList<String>();
		Map<String,Integer> stringNum = new HashMap<String,Integer>();
		
		for(int i = 0;i<100;i++){
			String strTmp = String.valueOf(stringkey.charAt(i%33))
			+String.valueOf(stringkey.charAt((i%33)+1+(i/33)));
			numString.add(strTmp);
			stringNum.put(strTmp, i);
		}
		list.add(numString);
		list.add(stringNum);
		return list;
	}
	
	 public static void main(String[] args) {
		 String sid = "s9f5a24gqtfg";
		 int id = 2;
		 int outInt = CodeIDOperator.codeToId(sid);
		 String outSid = CodeIDOperator.idToCode(id);
		 System.out.println("the sid is  :" + sid + ": result is :" +outInt);
		 System.out.println("the id is  :" + id + ": result is :" +outSid);
	}
	
}
