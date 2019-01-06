package com.yiyue.store;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by zm on 2018/9/10.
 */
public class _YLApplication extends YLApplication{

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        /**
         * chrome://inspect
         * 可查看本地数据库 对网络请求进行抓包
         */
        Stetho.initializeWithDefaults(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        LeakCanary.install(this);
    }

}
