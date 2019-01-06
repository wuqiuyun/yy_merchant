package com.yiyue.store.module.common;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.local.preferences.SystemConfigSharedPreferences;
import com.yiyue.store.module.login.LoginActivity;
import com.yiyue.store.module.main.MainActivity;
import com.yiyue.store.widget.ImageViewPager;

import java.util.ArrayList;

public class GuideActivity extends BaseAppCompatActivity {

    ImageViewPager splashViewpager;
    ImageView mImageView;

    private final ArrayList<View> views = new ArrayList<>();
    private int stateScroll;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    public void init() {
        splashViewpager = findViewById(R.id.splash_viewpager);

        addGuideBg(R.drawable.bg_guide_1, false);
        addGuideBg(R.drawable.bg_guide_2, true);
        addGuideBg(R.drawable.bg_guide_3, true);
        addGuideBg(R.drawable.bg_guide_4, true);
        splashViewpager.setViewPagerViews(views);

        splashViewpager.setOnPagerChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /**
                 * 最后一页继续向右滑动则跳转
                 */
                if (position == views.size() - 1 && stateScroll == ViewPager.SCROLL_STATE_DRAGGING) {
                    jmp();
                }
                if (position != views.size() - 1) {
                    mImageView.setVisibility(View.GONE);
                } else {
                    mImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                stateScroll = state;
            }
        });

        // 跳转至首页
        mImageView = findViewById(R.id.btn_startHome);
        mImageView.setOnClickListener(v -> jmp());
    }

    private void addGuideBg(int bg, boolean isWait) {
        ImageView imageView = new ImageView(this);
        if (isWait) {
            try {
                Glide.with(getApplicationContext()).load("android.resource://com.yiyue.store/drawable/" + bg).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
                imageView.setImageResource(bg);
            }
        } else {
            imageView.setImageResource(bg);
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        views.add(imageView);
        imageView.setOnClickListener(v -> {
            /**
             * 最后一页点击跳转
             */
            if (splashViewpager.getCurrentItem() == views.size() - 1) {
                jmp();
            }
        });
    }

    /**
     * 跳转到主页,并标志引导页已被查阅，下次将不再出现
     */
    private void jmp() {
        SystemConfigSharedPreferences.getInstance().setIsShowGuide(false);
        if (AccountManager.getInstance().isLogin()) {
            MainActivity.startActivity(this, MainActivity.HOME);
            finish();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        LoginActivity.startActivity(GuideActivity.this, LoginActivity.class);
        finish();
    }
}
