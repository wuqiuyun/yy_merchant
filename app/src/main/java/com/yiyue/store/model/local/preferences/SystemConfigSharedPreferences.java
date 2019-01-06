package com.yiyue.store.model.local.preferences;

import android.text.TextUtils;

import com.yiyue.store.YLApplication;
import com.yl.core.component.log.DLog;

/**
 * Created by zm on 2018/11/28.
 */
public class SystemConfigSharedPreferences {

    private static SystemConfigSharedPreferences sInstance;

    private SharedPfUtils mSharedPfUtils;

    private SystemConfigSharedPreferences() {
        mSharedPfUtils = new SharedPfUtils(YLApplication.getContext());
    }


    public static synchronized SystemConfigSharedPreferences getInstance() {
        if(sInstance == null) {
            sInstance = new SystemConfigSharedPreferences();
        }
        return sInstance;
    }

    /**
     * 是否显示引导页面
     */
    private static String SYSTEM_ISSHOW_GUIDE = "ISSHOW_GUIDE";

    public void setIsShowGuide(boolean isShowGuide) {
        mSharedPfUtils.saveString(SYSTEM_ISSHOW_GUIDE, isShowGuide ? "1" : "0");
    }

    public boolean isShowGuide() {
        boolean result = true;
        try {
            String isShow = mSharedPfUtils.getString(SYSTEM_ISSHOW_GUIDE, "1");
            if (TextUtils.isEmpty(isShow)) {
                return true;
            }
            result = "1".equals(isShow);
        } catch (Exception e) {
            e.printStackTrace();
            DLog.e(e);
        }
        return result;
    }
}
