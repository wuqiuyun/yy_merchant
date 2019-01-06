package com.yiyue.store.util;

/**
 * Created by zm on 2017/5/24.
 */

public class TypeConvertUtils {

    /**
     * @param value
     * @param defaultValue
     * @return integer
     * @Title: convertToInt
     * @Description: 对象转化为整数数字类型
     */
    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * @param value
     * @param defaultValue
     * @return long
     * @Title: convertToLong
     * @Description: 对象转化为长整数数字类型
     */
    public final static long convertToLong(Object value, long defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).longValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * @param value
     * @param defaultValue
     * @return double
     * @Title: convertToDouble
     * @Description: 对象转化为双精度数数字类型
     */
    public final static double convertToDouble(Object value, double defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).doubleValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }
    /**
     * @param value
     * @param defaultValue
     * @return float
     * @Title: convertToFloat
     * @Description: 对象转化为单精度数数字类型
     */
    public final static float convertToFloat(Object value, float defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(value.toString());
        } catch (Exception e) {
            try {
                return Float.valueOf(value.toString()).floatValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    /**
     * @param value
     * @param defaultValue
     * @return String
     * @Title: convertToSting
     * @Description: 对象转化字符串数数字类型
     */
    public final static String convertToString(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
