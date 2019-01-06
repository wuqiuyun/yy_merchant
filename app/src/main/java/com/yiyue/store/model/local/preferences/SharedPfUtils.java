package com.yiyue.store.model.local.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zzz on 2018/9/4.
 * SharedPreferences工具类
 */

public class SharedPfUtils {
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;
    private final String TAG="SharedPfUtils";

    public SharedPfUtils(Context context){
        mSharedPreferences= context
                .getSharedPreferences(context.getApplicationContext().getPackageName()
                        , Context.MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
    }

    /**
     * 存储boolean类型的数据
     * @param key
     * @param value
     */
    public void saveBoolean(String key, boolean value){
        mEditor.putBoolean(key,value).commit();
    }

    /**
     * 获取boolean类型的数据
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue){
        return mSharedPreferences.getBoolean(key,defValue);
    }

    /**
     * 存储int类型的数据
     * @param key
     * @param value
     */
    public void saveInt(String key, int value){
        mEditor.putInt(key,value).commit();
    }

    /**
     * 获取int类型的数据
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue){
        return mSharedPreferences.getInt(key,defValue);
    }

    /**
     * 存储float类型的数据
     * @param key
     * @param value
     */
    public void saveFloat(String key, float value){
        mEditor.putFloat(key,value).commit();
    }

    /**
     * 获取float类型的数据
     * @param key
     * @param defValue
     * @return
     */
    public float getFloat(String key, float defValue){
        return mSharedPreferences.getFloat(key,defValue);
    }

    /**
     * 存储String类型的数据
     * @param key
     * @param value
     */
    public void saveString(String key, String value){
        mEditor.putString(key,value).commit();
    }

    /**
     * 获取String类型的数据
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue){
        return mSharedPreferences.getString(key,defValue);
    }
    /**
     * 存储long类型的数据
     * @param key
     * @param value
     */
    public void saveLong(String key, long value){
        mEditor.putLong(key,value).commit();
    }

    /**
     * 获取long类型的数据
     * @param key
     * @param defValue
     * @return
     */
    public long getLong(String key, long defValue){
        return mSharedPreferences.getLong(key,defValue);
    }

    /**
     * 通过Gson转换成json格式的String进行存储对象
     * @param key
     * @param value
     */
    public void saveObject(String key, Object value){
        this.saveString(key,new Gson().toJson(value));
    }

    /**
     * 通过Gson去解析json格式，转换回对象返回
     * 如果为空，则返回null
     * @param key
     * @param classType
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> classType){
        String objectInfo=getString(key,"");
        if (objectInfo!=null && !objectInfo.isEmpty()){
            T result=null;
            try{
                result = new Gson().fromJson(objectInfo,classType);
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
            return result;
        }else {
            return null;
        }
    }

    /**
     * 保存对象列表
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void saveList(String key, List<T> value){
        saveString(key,new Gson().toJson(value));
    }

    /**
     * 获取对象列表，如果为空，则返回null
     * @param key
     * @param typeToken
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String key, TypeToken<List<T>> typeToken){
        String objectInfo=getString(key,"");
        if (objectInfo!=null && !objectInfo.isEmpty()){
            List<T> result=null;
            try{
                result=new Gson().fromJson(objectInfo,typeToken.getType());
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
            return result;
        }else {
            return null;
        }
    }

    /**
     * 清除所有缓存
     */
    public void clearAll(){
        mEditor.clear();
        mEditor.commit();
    }

    public void remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }
}
