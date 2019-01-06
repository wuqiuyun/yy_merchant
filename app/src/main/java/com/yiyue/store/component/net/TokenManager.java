package com.yiyue.store.component.net;

import android.text.TextUtils;

import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.local.preferences.AccountSharedPreferences;

/**
 * Created by zm on 2018/10/15.
 */

public class TokenManager {

    public static final String TOKEN_KEY = "token";

    /**
     * 全局token
     */
    public static String sToken = "";

    public static String getToken() {
        if(TextUtils.isEmpty(sToken)) {
            initToken();
        }
        return sToken;
    }

    /**
     * 初始化token
     */
    public static String initToken() {
        // 从本地读取
        sToken = AccountSharedPreferences.getInstance().getToken();

        if (TextUtils.isEmpty(sToken)) {
            if (AccountManager.getInstance().getAccount() != null) {
                sToken = AccountManager.getInstance().getAccount().getToken();
                if (!TextUtils.isEmpty(sToken)) {
                    updateToken(sToken);
                }
            }
        }

        // 从内存读取
        return sToken;
    }

    /**
     * 更新token
     */
    public static void updateToken(String token) {

        // 写到本地
        AccountSharedPreferences.getInstance().updateToken(token);

        // 写到内存
        sToken = token;
    }

    public static void clearToken() {
        sToken = null;
        // 清除本地Token
        AccountSharedPreferences.getInstance().clearToken();
    }
}
