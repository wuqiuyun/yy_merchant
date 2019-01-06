package com.yiyue.store.util;

import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.yiyue.store.R;

/**
 * Created by lyj on 2019/1/3
 */
public class BankImgUtil {

    /**
     * 获取银行卡图片
     * @param imgNa
     */
    public static int getBankImg(String imgNa) {
        @DrawableRes int resId = 0;
        if (imgNa==null|| TextUtils.isEmpty(imgNa)) return R.drawable.icon_boc_nor;
        if (imgNa.equals("ICBC")){
            resId = R.drawable.icon_boc_nor;
        }else if (imgNa.equals("CBC")){
            resId = R.drawable.icon_cbc_nor;
        }else if (imgNa.equals("ABC")){
            resId = R.drawable.icon_abc_nor;
        }else if (imgNa.equals("BOC")){
            resId = R.drawable.icon_boc_nor;
        }else if (imgNa.equals("BCM")){
            resId = R.drawable.icon_bcm_nor;
        }else if (imgNa.equals("CMB")){
            resId = R.drawable.icon_cncb_nor;
        }else if (imgNa.equals("CIB")){
            resId = R.drawable.icon_cib_nor;
        }else if (imgNa.equals("PSBC")){
            resId = R.drawable.icon_psbc_nor;
        }else if (imgNa.equals("CNCB")){
            resId = R.drawable.icon_cncb_nor;
        }else if (imgNa.equals("CMBC")){
            resId = R.drawable.icon_cmbc_nor;
        }else if (imgNa.equals("PAB")){
            resId = R.drawable.icon_pab_nor;
        }else if (imgNa.equals("CEB")){
            resId = R.drawable.icon_ceb_nor;
        }
        return resId;
    }

    /**
     * 获取银行卡图片2
     * @param imgNa
     */
    public static int getBankImgTwo(String imgNa) {
        @DrawableRes int resId = 0;
        if (imgNa==null|| TextUtils.isEmpty(imgNa)) return R.drawable.icon_icbc1_nor;
        if (imgNa.equals("ICBC")){
            resId = R.drawable.icon_icbc1_nor;
        }else if (imgNa.equals("CBC")){
            resId = R.drawable.icon_cbc1_nor;
        }else if (imgNa.equals("ABC")){
            resId = R.drawable.icon_abc1_nor;
        }else if (imgNa.equals("BOC")){
            resId = R.drawable.icon_boc1_nor;
        }else if (imgNa.equals("BCM")){
            resId = R.drawable.icon_bcm1_nor;
        }else if (imgNa.equals("CMB")){
            resId = R.drawable.icon_cncb1_nor;
        }else if (imgNa.equals("CIB")){
            resId = R.drawable.icon_cib1_nor;
        }else if (imgNa.equals("PSBC")){
            resId = R.drawable.icon_psbc1_nor;
        }else if (imgNa.equals("CNCB")){
            resId = R.drawable.icon_cncb1_nor;
        }else if (imgNa.equals("CMBC")){
            resId = R.drawable.icon_cmbc1_nor;
        }else if (imgNa.equals("PAB")){
            resId = R.drawable.icon_pab1_nor;
        }else if (imgNa.equals("CEB")){
            resId = R.drawable.icon_ceb1_nor;
        }
        return resId;
    }

    /**
     * 获取银行卡背景图片
     * @param imgNa
     */
    public static int getBankImgBag(String imgNa) {
        @DrawableRes int resId = 0;
        if (imgNa==null|| TextUtils.isEmpty(imgNa)) return R.drawable.background_icbc;
        if (imgNa.equals("ICBC")){
            resId = R.drawable.background_icbc;
        }else if (imgNa.equals("CBC")){
            resId = R.drawable.background_cbc;
        }else if (imgNa.equals("ABC")){
            resId = R.drawable.background_abc;
        }else if (imgNa.equals("BOC")){
            resId = R.drawable.background_boc;
        }else if (imgNa.equals("BCM")){
            resId = R.drawable.background_bcm;
        }else if (imgNa.equals("CMB")){
            resId = R.drawable.background_cmb;
        }else if (imgNa.equals("CIB")){
            resId = R.drawable.background_cib;
        }else if (imgNa.equals("PSBC")){
            resId = R.drawable.background_psbc;
        }else if (imgNa.equals("CNCB")){
            resId = R.drawable.background_cncb;
        }else if (imgNa.equals("CMBC")){
            resId = R.drawable.background_cmbc;
        }else if (imgNa.equals("PAB")){
            resId = R.drawable.background_pab;
        }else if (imgNa.equals("CEB")){
            resId = R.drawable.background_ceb;
        }
        return resId;
    }
}

