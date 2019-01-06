package com.yiyue.store.component.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.preferences.SharePreferencesUtils;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.ImMessageBean;
import com.yiyue.store.model.vo.bean.daobean.OrderMessageBean;
import com.yiyue.store.model.vo.bean.daobean.SysMessageBean;
import com.yiyue.store.module.home.evaluation.EvaluationManagerActivity;
import com.yiyue.store.module.home.order.details.OrderDetailsActivity;
import com.yiyue.store.module.im.daoutil.ImMessageDaoUtils;
import com.yiyue.store.module.im.daoutil.OrderMessageDaoUtils;
import com.yiyue.store.module.im.daoutil.SysMessageDaoUtils;
import com.yiyue.store.util.NotificationUtils;
import com.yl.core.component.log.DLog;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by zhangzz on 2018/10/20
 * 阿里云推送接收Receiver
 */
public class AliPushMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";
    private OrderMessageDaoUtils orderMessageDaoUtils;
    private ImMessageDaoUtils imMessageDaoUtils;
    private SysMessageDaoUtils sysMessageDaoUtils;
    private static Ringtone mRingtone;
    private boolean isShow = true;//显示消息是否

    /**
     * 推送通知回调
     * 开启新消息通知，使用通知处理消息
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        DLog.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        setNotification(context);
        //{_ALIYUN_NOTIFICATION_ID_=821530, 
        // code=1, 
        // data={"orderNo":"djskaf;s","orderTime":1542094292862,"orderId":1,"serverName":"洗剪套餐","title":"","msgtype":0,"content":""}, _
        // ALIYUN_NOTIFICATION_PRIORITY_=1}
        Gson gs = new Gson();
        String code = "0";
        String objectStr = null;
        for (Map.Entry<String, String> str : extraMap.entrySet()) {
            if ((str.getKey()).equals("code")) {
                code = str.getValue();
            }

            if ((str.getKey()).equals("data")) {
                objectStr = str.getValue();
            }

        }
        switch (Integer.valueOf(code)) {
            case 1://订单消息
                OrderMessageBean orderMessage = gs.fromJson(objectStr, OrderMessageBean.class);
                if (null != orderMessage) {
                    orderMessageDaoUtils = new OrderMessageDaoUtils(context);
                    orderMessageDaoUtils.insert(orderMessage);
                    SharePreferencesUtils.getSharePreferencesUtils().setSysNoticeShow(true);
                    EventBus.getDefault().post(new EventBean.NewMessage());//更新消息页红点
                    EventBus.getDefault().post(new EventBean.NewOrderMessage());//刷新对应分页的内容
                    if (orderMessage.getMsgtype() == 14||orderMessage.getMsgtype() == 16){//美发师收到了新的订单//时间修改
                        playSound(YLApplication.getContext(),1);
                    }else if (orderMessage.getMsgtype() == 10){//服务已取消
                        playSound(YLApplication.getContext(),2);
                    }else {
                        playSound(YLApplication.getContext(),3);
                    }/*else if (orderMessage.getMsgtype() == 7){//门店收到了当面付成功消息
                        playSound(YLApplication.getContext(),3);
                    }else if (orderMessage.getMsgtype() == 5){//服务完成
                        playSound(YLApplication.getContext(),3);
                    }else if (orderMessage.getMsgtype() == 15){//已评价
                        playSound(YLApplication.getContext(),3);
                    }*/
                }

                break;
            case 2://互动消息
                ImMessageBean imMessage = gs.fromJson(objectStr, ImMessageBean.class);
                if (null != imMessage && !TextUtils.isEmpty(imMessage.getId())) {
                    imMessageDaoUtils = new ImMessageDaoUtils(context);
                    imMessageDaoUtils.insertOrUpdate(imMessage, imMessage.getId());
                    SharePreferencesUtils.getSharePreferencesUtils().setSysNoticeShow(true);
                    EventBus.getDefault().post(new EventBean.NewMessage());

                    if (imMessage.getStatus() == 1) {
                        EventBus.getDefault().post(new EventBean.FriendChangeEventBean(Constants.EVENT_FRIEND_CHANGE));
                    }
                    playSound(YLApplication.getContext(),3);
                }
                break;
            case 3://系统通知
                SysMessageBean sysMessage = gs.fromJson(objectStr, SysMessageBean.class);
                if (null != sysMessage) {
                    sysMessageDaoUtils = new SysMessageDaoUtils(context);
                    sysMessageDaoUtils.insert(sysMessage);
                    SharePreferencesUtils.getSharePreferencesUtils().setSysNoticeShow(true);
                    EventBus.getDefault().post(new EventBean.NewMessage());
                    EventBus.getDefault().post(new EventBean.NewSysMessage());
                    playSound(YLApplication.getContext(),3);
                }
                break;
        }
    }

    /**
     * 推送消息回调
     * 1、互动消息 状态变动时，使用消息
     * 2、关闭通知接收开关，所有通知改用消息
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        DLog.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        buildNotification(context, cPushMessage);
    }

    /**
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        DLog.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        //extraMap:
        //{
        //"_ALIYUN_NOTIFICATION_ID_":"349159",
        //"code":"1",
        //"data":"{\"orderNo\":\"djskaf;s\",\"ordertime\":1542094292862,\"createtime\":1542094292862,\"orderId\":458,\"serverName\":\"洗剪套餐\",\"title\":\"接单成功\",\"msgtype\":0,\"content\":\"您预约的服务已被美发师接单，请注意预约时间\"}"
        //}
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(extraMap);
        int code = jsonObject.get("code").getAsInt();
        if (code == 1) {//订单消息
            String str = jsonObject.get("data").getAsString();
            DLog.e("MyMessageReceiver", "data: " + str);
            JsonObject objectStr = (JsonObject) new JsonParser().parse(str);
            if (null != objectStr) {
                OrderMessageBean orderMessage = new Gson().fromJson(objectStr, OrderMessageBean.class);
                if (null != orderMessage && orderMessage.getOrderId() != 0) {
                    if (orderMessage.getMsgtype()==15){//评论
                        Bundle EvaluationBundle = new Bundle();
                        EvaluationBundle.putInt(Constants.EVALUATION_TYPE, 1);
                        EvaluationManagerActivity.startActivity(context , EvaluationManagerActivity.class ,EvaluationBundle);
                    }else {
                        OrderDetailsActivity.startActivity(context, orderMessage.getOrderId() + "");
                    }
                }
            }
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        DLog.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        DLog.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        DLog.e("MyMessageReceiver", "onNotificationRemoved");
    }

    /**
     * 自8.0(API Level 26)起，Android 推出了NotificationChannel机制，旨在对通知进行分类管理。
     * 如果用户App的targetSdkVersion大于等于26，且并未设置NotificaitonChannel，创建的通知是不会弹出的。
     *
     * @param context
     */
    private void setNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    //播放自定义的声音
    public synchronized void playSound(Context context,int isCancel) {
//        if (mRingtone == null) {
        DLog.d("----------初始化铃声----------");
        String uri = null;
        Uri no;
        if (isCancel==1){
            uri = "android.resource://" + context.getPackageName() + "/" + R.raw.new_order_message;
            no = Uri.parse(uri);
        }else if (isCancel==2){
            uri = "android.resource://" + context.getPackageName() + "/" + R.raw.cancel_order_message;
            no = Uri.parse(uri);
        }else {
            no = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        mRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), no);
        mRingtone.play();
//        }
        //避 免重复逐条播放
//        if (!mRingtone.isPlaying()) {
//            DLog.e("--------------播放铃声---------------" + mRingtone.isPlaying());
//            mRingtone.play();
//        }
    }

    /**
     * 接受到对应消息后，消息的弹出处理
     */
    public void buildNotification(Context context, CPushMessage message) {
        isShow = AccountManager.getInstance().getUserShutdown() == 0;

        String strTitle = "";
        String strContent = "";

        JsonArray list = (JsonArray) new JsonParser().parse(message.getContent());
        JsonObject jsonObject = (JsonObject) list.get(0);
        JsonObject objectStr = jsonObject.get("data").getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        switch (code) {
            case 1: //订单消息
                if (null != objectStr) {

                    OrderMessageBean orderMessage = new Gson().fromJson(objectStr, OrderMessageBean.class);
                    if (null != orderMessage) {
                        strTitle = orderMessage.getTitle();
                        strContent = orderMessage.getContent();
                        orderMessageDaoUtils = new OrderMessageDaoUtils(context);
                        orderMessageDaoUtils.insert(orderMessage);
                        SharePreferencesUtils.getSharePreferencesUtils().setSysNoticeShow(true);
                        EventBus.getDefault().post(new EventBean.NewMessage());//更新消息页红点
                        EventBus.getDefault().post(new EventBean.NewOrderMessage());//刷新对应分页的内容
                        if (isShow){
                            if (orderMessage.getMsgtype() == 14||orderMessage.getMsgtype() == 16){//美发师收到了新的订单//时间修改
                                playSound(YLApplication.getContext(),1);
                            }else if (orderMessage.getMsgtype() == 10){//服务已取消
                                playSound(YLApplication.getContext(),2);
                            }else {
                                playSound(YLApplication.getContext(),3);
                            }
                        }
                    }else {
                        DLog.e("MyMessageReceiver", "解析订单通知data失败");
                    }
                } else {
                    DLog.e("MyMessageReceiver", "消息data为空");
                }
                break;
            case 2: //互动消息
                if (null != objectStr) {
                    ImMessageBean imMessage = new Gson().fromJson(objectStr, ImMessageBean.class);
                    if (null != imMessage && !TextUtils.isEmpty(imMessage.getId())) {
                        //发起申请者是否为自己这一方
                        boolean isSelf = String.valueOf(imMessage.getRequestUserId()).equals(AccountManager.getInstance().getUserId());
                        switch (imMessage.getType()) {
                            case 1://好友申请
                                //如果申请人id是自己 则是自己向他人加好友
                                if(!isSelf){
                                    if (imMessage.getStatus()==0){
                                        strTitle = imMessage.getFriendNickname();
                                        strContent = strTitle+" 申请加您为好友";
                                    }else if (imMessage.getStatus()==1){
                                        strTitle = "申请已同意";
                                        strContent = "我通过了你的朋友验证请求，现在我们可以开始聊天了";
                                    }else  if (imMessage.getStatus()==2){
                                        strTitle = "申请已拒绝";
                                        strContent = imMessage.getFriendNickname()+"拒绝了你的好友申请";
                                    }
                                }else {
                                    if (imMessage.getStatus()==0){
                                        return;
                                    }else if (imMessage.getStatus()==1){
                                        strTitle = "申请已同意";
                                        strContent = "我通过了你的朋友验证请求，现在我们可以开始聊天了";
                                    }else  if (imMessage.getStatus()==2){
                                        strTitle = "申请已拒绝";
                                        strContent = imMessage.getFriendNickname()+"拒绝了你的好友申请";
                                    }
                                }

                                break;

                            case 2://加群申请
                                //如果申请人id是自己 则是自己申请入群
                                if(!isSelf){
                                    if (imMessage.getStatus()==0) {
                                        strTitle = imMessage.getFriendNickname();
                                        strContent = strTitle + " 申请加入您的群";
                                    }else if (imMessage.getStatus()==1){
                                        strTitle = "申请已同意";
                                        strContent = "管理员通过了你加群请求，现在可以开始聊天了";
                                    }else  if (imMessage.getStatus()==2){
                                        strTitle = "申请已拒绝";
                                        strContent = "管理员拒绝了你的加群申请";
                                    }
                                }else {

                                }
                                break;

                            case 3://邀请加群
                                strTitle = imMessage.getFriendNickname();
                                strContent = "群主邀请您加入 " + strTitle;
                                break;
                        }

                        imMessageDaoUtils = new ImMessageDaoUtils(context);
                        imMessageDaoUtils.insertOrUpdate(imMessage, imMessage.getId());
                        SharePreferencesUtils.getSharePreferencesUtils().setSysNoticeShow(true);
                        EventBus.getDefault().post(new EventBean.NewMessage());
                        if (imMessage.getStatus() == 1) {
                            EventBus.getDefault().post(new EventBean.FriendChangeEventBean(Constants.EVENT_FRIEND_CHANGE));
                        }
                        if (isShow){
                            playSound(YLApplication.getContext(),3);
                        }
                    } else {
                        DLog.e("MyMessageReceiver", "解析互动消息data失败");
                    }
                } else {
                    DLog.e("MyMessageReceiver", "消息data为空");
                }
                break;

            case 3: //系统消息
                if (null != objectStr) {
                    SysMessageBean sysMessage = new Gson().fromJson(objectStr, SysMessageBean.class);
                    if (null != sysMessage) {
                        strTitle = sysMessage.getTitle();
                        strContent = sysMessage.getContent();
                        sysMessageDaoUtils = new SysMessageDaoUtils(context);
                        sysMessageDaoUtils.insert(sysMessage);
                        SharePreferencesUtils.getSharePreferencesUtils().setSysNoticeShow(true);
                        EventBus.getDefault().post(new EventBean.NewMessage());
                        EventBus.getDefault().post(new EventBean.NewSysMessage());
                        if (isShow){
                            playSound(YLApplication.getContext(),3);
                        }
                    } else {
                        DLog.e("MyMessageReceiver", "解析系统消息data失败");
                    }
                } else {
                    DLog.e("MyMessageReceiver", "消息data为空");
                }
                break;
        }
        if (isShow){
            NotificationUtils notificationUtils = new NotificationUtils(context);
            notificationUtils.sendNotification(strTitle,strContent,message.hashCode(),message);
        }
    }

}
