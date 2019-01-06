package com.yl.core.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.yl.core.component.log.DLog;

import java.util.List;

/**
 * Created by zm on 2018/10/30.
 */
public class ClientUtil {

    public static String getAppPackageName(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        DLog.d("当前应用包名:" + componentInfo.getPackageName());
        return componentInfo.getPackageName();
    }
}
