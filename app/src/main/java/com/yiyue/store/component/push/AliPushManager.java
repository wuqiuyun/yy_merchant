package com.yiyue.store.component.push;

import android.media.RingtoneManager;
import android.net.Uri;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.yiyue.store.YLApplication;
import com.yl.core.component.log.DLog;

/**
 * Created by zhangzz on 2018/10/20
 * 阿里云推送管理类
 */
public class AliPushManager {
    private static final String TAG = "AliPushManager";

    private static AliPushManager aliPushManager;
    private CloudPushService pushService;

    public static AliPushManager getInstance() {
        if (aliPushManager == null) {
            aliPushManager = new AliPushManager();
        }
        return aliPushManager;
    }

    /**
     * 初始化云推送通道
     *
     * @param
     */
    public void initPushService() {
        try {
            PushServiceFactory.init(YLApplication.getContext());
            PushServiceFactory.getCloudPushService().setLogLevel(CloudPushService.LOG_INFO);//阿里云推送debug
            pushService = PushServiceFactory.getCloudPushService();
            pushService.register(YLApplication.getContext(), new CommonCallback() {
                @Override
                public void onSuccess(String response) {
                    DLog.i(TAG, "init cloudchannel success " +      pushService.getDeviceId());
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    PushServiceFactory.getCloudPushService().setNotificationSoundFilePath(uri.toString());
                    //setConsoleText("init cloudchannel success");
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    DLog.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                    //setConsoleText("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                }
            });
        } catch (Exception e) {
            DLog.i(TAG, "initPushService crash 不处理");
        }
    }

    /**
     * 绑定一个账号
     *
     * @param account
     */
    public void bindAccount(String account) {
        if (pushService != null) {
            pushService.bindAccount(account, new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    DLog.i(TAG, "bindAccount success");
                }

                @Override
                public void onFailed(String s, String s1) {
                    DLog.i(TAG, "bindAccount failed");
                }
            });
        }
    }

    /**
     * 退出时解除绑定一个账号
     */
    public void unbindAccount() {
        if (pushService != null) {
            pushService.unbindAccount(new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    DLog.i(TAG, "unbindAccount success");
                }

                @Override
                public void onFailed(String s, String s1) {
                    DLog.i(TAG, "unbindAccount failed");
                }
            });
        }
    }

    /**
     * 关闭推送通道
     * 全量推送场景下，关闭推送通道存在2-3小时延迟。其他场景实时生效
     */
    public void turnOffPushChannel(){
        if (pushService != null) {
            pushService.turnOffPushChannel(new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    DLog.i(TAG, "turnOffPushChannel success");
                }

                @Override
                public void onFailed(String s, String s1) {
                    DLog.i(TAG, "turnOffPushChannel failed");
                }
            });
        }
    }

    /**
     * 开启推送通道
     * 全量推送场景下，开启推送通道存在2-3小时延迟。其他场景实时生效
     */
    public void turnOnPushChannel(){
        if (pushService != null) {
            pushService.turnOnPushChannel(new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    DLog.i(TAG, "turnOnPushChannel success");
                }

                @Override
                public void onFailed(String s, String s1) {
                    DLog.i(TAG, "turnOnPushChannel failed");
                }
            });
        }
    }

    /**
     * 设置免打扰时段
     * 关闭新消息通知后，设置为全天免打扰
     */
    public void setDoNotDisturb(boolean isSet){
        if (pushService != null) {
            if (isSet){
                pushService.setDoNotDisturb(0, 0, 23, 59, new CommonCallback() {
                    @Override
                    public void onSuccess(String s) {
                        DLog.i(TAG, "setDoNotDisturb 0:0-23:59 success");
                    }

                    @Override
                    public void onFailed(String s, String s1) {
                        DLog.i(TAG, "setDoNotDisturb 0:0-23:59 success");
                    }
                });
            } else {
                pushService.closeDoNotDisturbMode();
            }
        }
    }
}
