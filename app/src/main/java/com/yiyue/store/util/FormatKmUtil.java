package com.yiyue.store.util;


import java.math.BigDecimal;

/**
 * Created by lyj on 2018/10/20.
 * <1000 m
 * >1000 /1000 km
 * >999km 999km+
 */
public class FormatKmUtil {

	public static String FormatKmStr(double location) {
		 if(location>1000&&1000000>location){
			return MathematicsUtils.sub1(location/1000)+"km";
		}else if(1000000<=location){
			return "999km+";
		}
		return location+"m";
	}
}
