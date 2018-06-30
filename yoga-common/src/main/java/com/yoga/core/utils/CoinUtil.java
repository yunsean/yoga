package com.yoga.core.utils;
/**
 * 货币转换
 * @author Skysea
 *
 */
public class CoinUtil {
	public static double toMoney(long money){
		return 1.0*money/100;
	}
	
	public static double toMoneyUnitOfTenThousand(long money){
		return toMoney(money)/10000;
	}
}
