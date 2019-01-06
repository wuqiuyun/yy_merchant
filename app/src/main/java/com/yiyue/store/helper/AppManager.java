package com.yiyue.store.helper;

import android.app.Activity;
import android.content.Context;

import com.yl.core.component.log.DLog;

import java.util.Stack;

/**
 * Activity管理器
 * <p>
 * Created by zm on 2018/9/29.
 */
public class AppManager {

    // Activity栈
    private Stack<Activity> activityStack;
    // 单例模式
    private volatile static AppManager instance;

    private AppManager() {

    }

    /**
     * 单例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.push(activity);
        printActivityStack();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (!activityStack.isEmpty()) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public  void appExit(Context context) {
        try {
            finishAllActivity();

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            DLog.e(e.getMessage());
        }
    }

    public void printActivityStack(){
        DLog.d(activityStack.toString());
    }
}
