package com.yiyue.store.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yiyue.store.BuildConfig;

/**
 * Created by zm on 2018/9/25.
 */
public class DebugTestMainActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        intent.setClassName(DebugTestMainActivity.this,
                TextUtils.isEmpty(BuildConfig.LaunchActivity)
                        ? "com.yiyue.store.module.splash._SplashActivity"
                        : BuildConfig.LaunchActivity);
        startActivity(intent);
        this.finish();
    }
}
