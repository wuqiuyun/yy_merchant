package com.yiyue.store.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Gson数据格式化
 * */
public class GsonUtils {

    private GsonUtils() {

    }

    public static <T>T fromJson(String json, Class<T> type){
        Gson gson = new Gson();
        return gson.fromJson(json,type);

    }

    public static <T> List<T> listFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
    }

}
