package com.yiyue.store.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zm on 2018/10/8.
 */
public class ClientUtil {

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @return 返回进程的名字
     */
    public static final String getProcessName(Context context) {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processName;
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info =
                    (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }
}
