package com.yiyue.store.util;

import android.text.TextUtils;
import android.util.Base64;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhangzz on 2018/9/26
 */
public class StringUtil {
    public static String getPinYin(String inputString) {
        StringBuffer output = new StringBuffer("");

        //-------------------指定格式转换----------------------------
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // UPPERCASE：大写  (ZHONG)
        // LOWERCASE：小写  (zhong)
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // WITHOUT_TONE：无音标  (zhong)
        // WITH_TONE_NUMBER：1-4数字表示音标  (zhong4)
        // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // WITH_V：用v表示ü  (nv)
        // WITH_U_AND_COLON：用"u:"表示ü  (nu:)
        // WITH_U_UNICODE：直接用ü (nü)
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();

        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {
                    String[] temp = new String[0];
                    temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output.append(temp[0]);
                    output.append(" ");
                } else
                    output.append(Character.toString(input[i]));
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        return output.toString();
    }

    /**
     * 格式化手机号 188****1234
     * @param phoneNumber
     */
    public static String getPhoneNumber(String phoneNumber) {
        String newNum;

        if(TextUtils.isEmpty(phoneNumber)){
            newNum = "";
        }
        else if (!AccountValidatorUtil.isMobile(phoneNumber)) {
            newNum =  phoneNumber;
        }
        else {
            newNum = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return newNum;
    }

    /**
     * 格式化银行卡号 **** **** **** 1234
     * @param bankCardNumber
     */
    public static String getBankCardumber(String bankCardNumber) {
        String newNum;

        if(TextUtils.isEmpty(bankCardNumber)){
            newNum = "";
        }
        else if (bankCardNumber.length()< 4) {
            newNum =  "";
        }
        else {
            newNum = "****  ****  ****  "+bankCardNumber.substring(bankCardNumber.length()-4,bankCardNumber.length());
        }
        return newNum;
    }

    /**
     * Str--->base64
     * */
    public static String baseConvertStr(String str) {
        String encodedText = "";
        byte[] byteStr = new byte[0];
        try {
            byteStr = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        encodedText = Base64.encodeToString(byteStr,Base64.DEFAULT);
        return encodedText;
    }

}
