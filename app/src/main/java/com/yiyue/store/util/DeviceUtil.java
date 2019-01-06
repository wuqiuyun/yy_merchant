package com.yiyue.store.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yiyue.store.YLApplication;

import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Created by zm on 2018/9/25.
 */
public class DeviceUtil {

    /**
     * Return the unique device id.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    @RequiresPermission(READ_PHONE_STATE)
    public static String getDeviceId() {
        TelephonyManager tm =
                (TelephonyManager) YLApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //noinspection ConstantConditions
            @SuppressLint("MissingPermission") String imei = tm.getImei();
            if (!TextUtils.isEmpty(imei)) return imei;
            @SuppressLint("MissingPermission") String meid = tm.getMeid();
            return TextUtils.isEmpty(meid) ? "" : meid;
        }
        //noinspection ConstantConditions
        return tm.getDeviceId();
    }
}
