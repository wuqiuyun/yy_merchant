package com.yiyue.store.util;

import android.text.TextUtils;

import java.text.NumberFormat;

/**
 * Created by zm on 2018/9/25.
 */
public class FormatUtil {

	public static String Formatstring( String str) {
		if (TextUtils.isEmpty(str))
			return "";
		else if(str.trim().equals("null") && str.trim().equals("NULL"))
			return "";

		return str;
	}

	public static String FormatDouble(double doublemath) {
		NumberFormat nf = java.text.NumberFormat.getInstance();
		// 不使用千分位，即展示为11672283.234，而不是11,672,283.234
		nf.setGroupingUsed(false);
		// 设置数的小数部分所允许的最小位数
		nf.setMinimumFractionDigits(0);
		// 设置数的小数部分所允许的最大位数
		nf.setMaximumFractionDigits(5);

		return nf.format(doublemath);
	}

}
