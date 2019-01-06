package com.yiyue.store.module.splash;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.yiyue.store.BuildConfig;
import com.yiyue.store.R;
import com.yiyue.store.databinding.ViewSplashBinding;

/**
 * Created by zm on 2018/9/11.
 */
public class _SplashActivity extends SplashActivity{
    private ViewSplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = findViewById(R.id.fl_splash);
        View rooView = getLayoutInflater().inflate(R.layout.view_splash, null);
        binding = DataBindingUtil.bind(rooView);
        frameLayout.removeAllViews();
        frameLayout.addView(rooView);

        initView();
    }

    private void initView() {
        binding.tvServlet.setText(BuildConfig.API_HOST);
        binding.tvChannel.setText(BuildConfig.BUILD_TYPE);
        binding.tvVersion.setText(String.format(getString(R.string.common_version), BuildConfig.VERSION_NAME));
    }
}
