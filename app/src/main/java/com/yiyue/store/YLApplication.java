package com.yiyue.store;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mobstat.StatService;
import com.mob.MobSDK;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yiyue.store.component.greendao.DaoManager;
import com.yiyue.store.component.push.AliPushManager;
import com.yiyue.store.model.local.preferences.SharePreferencesUtils;
import com.yiyue.store.module.im.imlogin.ImLoginManager;
import com.yiyue.store.util.ClientUtil;
import com.yiyue.store.util.MediaLoader;
import com.yiyue.store.util.easyutils.AppFrontBackHelper;
import com.yiyue.store.util.easyutils.EasyHelper;
import com.yl.core.YLCore;
import com.yl.core.util.SystemUtil;

/**
 * Created by zm on 2018/9/8.
 */
public class YLApplication extends Application {
    private static final String TAG = "YLApplication";
    /**
     * Application Context
     */
    private static Context sContext;

    // 记录环信sdk是否已经初始化
    private boolean isInit = false;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private void init() {
        sContext = this.getApplicationContext();

        // 初始化日志
        SystemUtil.updateDeveloperLogControl(true);

        // 初始化核心库
        YLCore.init(this);
        // 初始化百度移动统计 只统计线上版本
        if ("release".equals(BuildConfig.BUILD_TYPE)) {
            initBaiduMobstat();
        }

        new DaoManager().init(sContext);//数据库更新工具类初始化

        AliPushManager.getInstance().initPushService();

        SharePreferencesUtils.initSharePreferencesUtils(this);

        // 初始化环信SDK
        initEasemob();

        //mob分享初始化
        MobSDK.init(this);
        //多图选择
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader()).build());
        appFront();
    }

    private void appFront() {
        AppFrontBackHelper helper = new AppFrontBackHelper();
        helper.register(YLApplication.this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                ImLoginManager.getInstance().isLoginIM();
            }

            @Override
            public void onBack() {
                //应用切到后台处理

            }
        });
    }

    /**
     * 获取全局Context
     *
     * @return context
     */
    public static Context getContext() {
        return sContext;
    }

    private void initEasemob() {
        String processAppName = ClientUtil.getProcessName(getApplicationContext());
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(sContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }

        // 调用初始化方法初始化sdk
        EasyHelper.getInstance().init(getApplicationContext());
        // 设置初始化已经完成
        isInit = true;
    }

    /**
     * 初始化百度移动统计
     */
    private void initBaiduMobstat() {
        // 打开调试开关，可以查看logcat日志。版本发布前，为避免影响性能，移除此代码
        // 查看方法：adb logcat -s sdkstat
        StatService.setDebugOn(true);


        // 开启自动埋点统计，为保证所有页面都能准确统计，建议在Application中调用。
        // IIgnoreAutoTrac，标记接口 实现IIgnoreAutoTrace接口，则自动埋点不会统计此页面
        // 第三个参数：autoTrackWebview：
        // 如果设置为true，则自动track所有webview；如果设置为false，则不自动track webview，
        // 如需对webview进行统计，需要对特定webview调用trackWebView() 即可。
        // 重要：如果有对webview设置过webchromeclient，则需要调用trackWebView() 接口将WebChromeClient对象传入，
        // 否则开发者自定义的回调无法收到。
        StatService.autoTrace(sContext, true, false);
        // 根据需求使用
        //StatService.autoTrace(sContext);
    }
}

