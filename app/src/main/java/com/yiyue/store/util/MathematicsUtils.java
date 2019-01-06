package com.yiyue.store.util;

import java.math.BigDecimal;

/**
 *
 * 计算工具类
 *
 * Created by lyj on 2018/11/21.
 */
public class MathematicsUtils {
    public static double sub(double v1, double v2) {//减法
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double div(double v1, double v2) {//除法
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,2,BigDecimal.ROUND_DOWN).doubleValue();//保留两位小数(截断)
    }

    public static double sub1(double v1) {//取两位小数
        BigDecimal bg = new BigDecimal(v1);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

}
