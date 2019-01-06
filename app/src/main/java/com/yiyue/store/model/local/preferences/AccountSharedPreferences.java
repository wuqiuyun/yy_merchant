package com.yiyue.store.model.local.preferences;

import com.yiyue.store.YLApplication;
import com.yiyue.store.model.vo.bean.UserBean;

/**
 * Created by zm on 2018/10/15.
 */
public class AccountSharedPreferences {
    private static final String ACCOUNT_TOKEN = "ACCOUNT_TOKEN";
    private static final String ACCOUNT_DATA = "ACCOUNT_DATA";

    private static AccountSharedPreferences sInstance;
    private SharedPfUtils mSharedPfUtils;

    private AccountSharedPreferences() {
        mSharedPfUtils = new SharedPfUtils(YLApplication.getContext());
    }

    public static synchronized AccountSharedPreferences getInstance() {
        if(sInstance == null) {
            sInstance = new AccountSharedPreferences();
        }
        return sInstance;
    }

    /**
     * 更新token
     * @param token
     */
    public void updateToken(String token) {
        mSharedPfUtils.saveString(ACCOUNT_TOKEN, token);
    }

    /**
     * 清除token
     */
    public void clearToken() {
        mSharedPfUtils.remove(ACCOUNT_TOKEN);
    }

    /**
     * 获取token
     * @return
     */
    public String getToken() {
        return mSharedPfUtils.getString(ACCOUNT_TOKEN, "");
    }

    /**
     * 获取用户数据
     * @return
     */
    public UserBean getAccountData() {
        return mSharedPfUtils.getObject(ACCOUNT_DATA, UserBean.class);
    }

    /**
     * 清除用户数据
     */
    public void clearAccountData() {
        mSharedPfUtils.remove(ACCOUNT_DATA);
    }

    /**
     * 更新用户数据
     * @param data
     */
    public void updateDataBase(UserBean data) {
        mSharedPfUtils.saveObject(ACCOUNT_DATA, data);
    }

}
