package com.yiyue.store.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.module.im.chat.ChatActivity;
import com.yiyue.store.module.main.MainActivity;

/**
 * Created by lyj on 2018/12/13.
 * 通知栏工具类
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    // 通知渠道的id
    public static final String id = "1";
    // 用户可以看到的通知渠道的名字.
    public static final CharSequence name = "notification channel";
    // 用户可以看到的通知渠道的描述
    public static final  String description = "notification description";
    private int clickNotificationCode=100;

    public NotificationUtils(Context context){
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        // 配置通知渠道的属性
        channel.setDescription(description);
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        // 设置通知出现时的震动（如果 android 设备支持的话）
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        getManager().createNotificationChannel(channel);
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content){
        return new Notification.Builder(getApplicationContext(), id)
                .setSmallIcon(R.mipmap.logo)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
    }
    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.logo)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
    }
    public void sendNotification(String title, String content, int code, CPushMessage message){
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
            notification.contentIntent = buildClickContent(YLApplication.getContext(), message);//点击通知事件
            notification.deleteIntent = buildDeleteContent(YLApplication.getContext(), message);//删除通知事件
            getManager().notify(code,notification);
        }else{
            Notification notification = getNotification_25(title, content).build();
            notification.contentIntent = buildClickContent(YLApplication.getContext(), message);//点击通知事件
            notification.deleteIntent = buildDeleteContent(YLApplication.getContext(), message);//删除通知事件
            getManager().notify(code,notification);
        }
    }

    /**
     * 群移出好友弹出移出提示
     * @param title
     * @param content
     * @param code
     */
    public void sendNormalNotification(String title, String content, int code){
        Notification notification;
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            Notification.Builder builder = getChannelNotification(title, content);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            notification = builder.build();
        }else{
            NotificationCompat.Builder builder = getNotification_25(title, content);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            notification = builder.build();
        }

        getManager().notify(code,notification);
    }

    public PendingIntent buildClickContent(Context context, CPushMessage message) {
        Intent clickIntent = new Intent();
        clickIntent.setAction("com.yiyue.store.click");
        //添加其他数据
        clickIntent.putExtra("messageBean",  message);//将message放入intent中，方便通知自建通知的点击事件
        return PendingIntent.getService(context, clickNotificationCode, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public PendingIntent buildDeleteContent(Context context, CPushMessage message) {
        Intent deleteIntent = new Intent();
        deleteIntent.setAction("com.yiyue.store.delete");
        //添加其他数据
        deleteIntent.putExtra("messageBean",  message);//将message放入intent中，方便通知自建通知的点击事件
        return PendingIntent.getService(context, clickNotificationCode, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}

