package com.yiyue.store.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.yl.core.component.log.DLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 读取Json文件的工具类
 * Create by zm on 2018/10/17
 */

public class GetJsonDataUtil {


    /**
     * 获取assets目录下的json文件数据
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            DLog.e(e, e.getMessage());
        }
        return stringBuilder.toString();
    }
}

