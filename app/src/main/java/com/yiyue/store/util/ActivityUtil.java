package com.yiyue.store.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by zm on 2018/10/24.
 */

public class ActivityUtil {

    /**
     * check current activity is finished
     *
     * @param activity the current activity
     * @return whether current activity is finished or not
     */
    public static boolean isActivityFinished(Activity activity) {
        if (null == activity) return true;
        if (activity.isFinishing()) return true;
        try {
            if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    public static boolean isExsitMianActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    /**
     * 判断activity是否处于栈顶
     *
     * @return true在栈顶false不在栈顶
     */
    public static boolean isMainActivityTop(Context context, Class<?> XXXActivity) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        if (name.equals(XXXActivity.getName())) {
            return true;
        } else {
            return false;
        }
    }

}
