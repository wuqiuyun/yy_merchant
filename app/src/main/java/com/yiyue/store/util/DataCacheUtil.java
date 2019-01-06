package com.yiyue.store.util;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yiyue.store.model.local.preferences.SharedPfUtils;
import com.yiyue.store.model.vo.bean.InviteMessageBean;

import java.util.List;

/**
 * Created by zzz on 2018/9/4.
 * 本地缓存sp的工具类
 */

public class DataCacheUtil {
    private SharedPfUtils mSharedPreferences;
    private static DataCacheUtil mInstance;

    /**
     * 通过单例模式实例化对象
     * @param context
     */
    private DataCacheUtil(Context context){
        mSharedPreferences=new SharedPfUtils(context);
    }

    /**
     * 调用构造函数时，传入context.getApplicationContext()
     * @param context
     * @return
     */
    public static DataCacheUtil getInstance(Context context){
        if (mInstance==null){
            synchronized (DataCacheUtil.class){
                if (mInstance==null){
                    mInstance=new DataCacheUtil(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存单个用户数据存储
     * @param str
     */
    public void saveString(String key, String str){
        mSharedPreferences.saveObject(key,str);
    }

    /**
     * 获取单个用户数据
     * @return
     */
    public String getString(String key){
        return mSharedPreferences.getString(key, "");
    }

    /**
     * 保存单个用户数据存储
     * @param user
     */
    public void saveUser(InviteMessageBean user){
        mSharedPreferences.saveObject("user_info",user);
    }

    /**
     * 获取单个用户数据
     * @return
     */
    public InviteMessageBean getUser(){
        return mSharedPreferences.getObject("user_info",InviteMessageBean.class);
    }


    /**
     * 保存List<InviteMessageBean>对象
     * @param users
     */
    public void saveInviteMsgList(List<InviteMessageBean> users){
        mSharedPreferences.saveList("ivmsg_list_info",users);
    }

    /**
     * 获取List<InviteMessageBean>对象
     * @return
     */
    public List<InviteMessageBean> getInviteMsgList(){
        return  mSharedPreferences.getList("ivmsg_list_info",new TypeToken<List<InviteMessageBean>>(){});
    }
}
