package com.yiyue.store.model.local.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * description: SharePreferences工具类
 * autour: luyongjin
 * date: 2017/5/15 17:38
 * update: 2017/5/15
 * version: 1.0.0
*/
public class SharePreferencesUtils {

	private Context context;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	private final String BALANCE_IS_SHOW = "BALANCE_IS_SHOW";// 是否显示余额
	private final String MESSAGE_IS_SHOW = "MESSAGE_IS_SHOW";// 是否打开消息提醒
	private final String SYS_NOTICE_POINT_IS_SHOW = "SYS_NOTICE_POINT_IS_SHOW";//是否显示IM的系统消息的红点
	private final String USER_RELANAME = "USER_RELANAME";// 真实姓名



	public static SharePreferencesUtils userDefaultInstance;

	private SharePreferencesUtils() {
		
	}

	public static SharePreferencesUtils initSharePreferencesUtils(Application application) {
		return userDefaultInstance = new SharePreferencesUtils(application);
	}

	public static SharePreferencesUtils getSharePreferencesUtils() {
		return userDefaultInstance;
	}

	private SharePreferencesUtils(Context contex) {
		this.context = contex;
		preferences = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		editor = preferences.edit();
	}

//	public int getStepTarget() {
//		return preferences.getInt(USER_STEP_TARGET, 10000);// 默认1w
//	}
//
//	public void setStepTarget(int stepTarget) {
//		this.editor.putInt(USER_STEP_TARGET, stepTarget).commit();
//	}
//

	public String getUserRealName() {
		return preferences.getString(USER_RELANAME, "");// 默认""
	}

	public void setRealName(String realName) {
		this.editor.putString(USER_RELANAME, realName).commit();
	}

	public boolean isBalanceShow() {
		return preferences.getBoolean(BALANCE_IS_SHOW, true);// 默认true 显示
	}

	public void setBalanceShow(boolean typeStatus) {
		this.editor.putBoolean(BALANCE_IS_SHOW, typeStatus).commit();
	}

	public boolean isMessageShow() {
		return preferences.getBoolean(MESSAGE_IS_SHOW, true);// 默认true 显示
	}

	public void setMessageShow(boolean typeStatus) {
		this.editor.putBoolean(MESSAGE_IS_SHOW, typeStatus).commit();
	}
	
	public boolean isSysNoticeShow() {
		return preferences.getBoolean(SYS_NOTICE_POINT_IS_SHOW, false);// 默认false 不显示红点
	}

	public void setSysNoticeShow(boolean typeStatus) {
		this.editor.putBoolean(SYS_NOTICE_POINT_IS_SHOW, typeStatus).commit();
	}

}
