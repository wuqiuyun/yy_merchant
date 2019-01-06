package com.yiyue.store.component.push;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yiyue.store.module.home.evaluation.EvaluationManagerActivity;
import com.yl.core.component.log.DLog;
import com.yiyue.store.YLApplication;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.daobean.OrderMessageBean;
import com.yiyue.store.module.home.order.details.OrderDetailsActivity;
import com.yiyue.store.module.splash.SplashActivity;
import com.yiyue.store.util.GsonUtils;

import java.util.List;

/**
 * Created by lyj on 2018/12/12
 * 阿里云推送 创建通知点击/删除事件接收Service
 */
public class NotificationService extends Service {
    public static final String TAG = "NotificationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return START_STICKY;
        }
        String action = intent.getAction();
        if (action.equals("com.yiyue.store.click")) {
            //添加您的通知点击处理逻辑
            CPushMessage message = intent.getParcelableExtra("messageBean");//获取message
            JsonArray list = (JsonArray) new JsonParser().parse(message.getContent());
            JsonObject jsonObject = (JsonObject) list.get(0);
            int code = jsonObject.get("code").getAsInt();
            if (code == 1){//订单消息
                String str = jsonObject.get("data").toString();
                DLog.e("NotificationService", "data: " + str);
                OrderMessageBean orderMessageBean = GsonUtils.fromJson(str,OrderMessageBean.class);
                if (null != orderMessageBean){
                    if (null != orderMessageBean && orderMessageBean.getOrderId() != 0) {
                        if (isAppRunning(YLApplication.getContext(),getPackageName())){//正在运行
                            if (orderMessageBean.getMsgtype()==15){//评论
                                Bundle EvaluationBundle = new Bundle();
                                EvaluationBundle.putInt(Constants.EVALUATION_TYPE, 1);
                                EvaluationManagerActivity.startActivity(YLApplication.getContext() , EvaluationManagerActivity.class ,EvaluationBundle);
                            }else {
                                OrderDetailsActivity.startActivity(YLApplication.getContext(),orderMessageBean.getOrderId()+"");
                            }
                        }else {
                            Intent launchIntent = YLApplication.getContext().getPackageManager().getLaunchIntentForPackage(getPackageName());
                            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            Bundle bundles = new Bundle();
                            bundles.putString(Constants.NOTIFICATION_MSG_TYPE, orderMessageBean.getMsgtype()+"");
                            bundles.putString(Constants.NOTIFICATION_DATA, orderMessageBean.getOrderId()+"");
                            launchIntent.setClass(YLApplication.getContext(), SplashActivity.class);
                            launchIntent.putExtras(bundles);
                            YLApplication.getContext().startActivity(launchIntent);
                        }
                    }
                }
            }else {
                Intent msgIntent =
                        YLApplication.getContext().getPackageManager().getLaunchIntentForPackage(getPackageName());//获取启动Activity
                startActivity(msgIntent);
            }

            PushServiceFactory.getCloudPushService().clickMessage(message);//上报通知点击事件，点击事件相关信息可以在推送控制台查看到
        } else if (action.equals("com.yiyue.store.delete")) {
            //添加您的通知删除处理逻辑
            CPushMessage message = intent.getParcelableExtra("messageBean");//获取message
            PushServiceFactory.getCloudPushService().dismissMessage(message);//上报通知删除事件，点击事件相关信息可以在推送控制台查看到
        }
        return flags;
    }


    /**
     * 方法描述：判断某一应用是否正在运行
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


}