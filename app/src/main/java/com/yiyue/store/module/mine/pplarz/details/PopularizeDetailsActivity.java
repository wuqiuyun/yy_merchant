package com.yiyue.store.module.mine.pplarz.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ActivityPopularizeDetailsBinding;
import com.yl.core.util.StatusBarUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class PopularizeDetailsActivity extends BaseAppCompatActivity {
    public static final String EXTRAS_ROLE = "ROLE";

    public static final int STORE = 1; // 门店
    public static final int STYLIST = 2; // 美发师
    public static final int USER = 3; // 用户

    @IntDef({STORE, STYLIST, USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Role {
    }

    private final String[] roleTitles = new String[]{"门店", "美发师", "用户"};
    private String[] labels = new String[2];
    private List<Fragment> fragments = new ArrayList<>();

    ActivityPopularizeDetailsBinding mBinding;
    @Role
    private int role; // 角色

    public static void actionStart(Context context, @Role int role) {
        Intent intent = new Intent();
        intent.setClass(context, PopularizeDetailsActivity.class);
        intent.putExtra(EXTRAS_ROLE, role);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_popularize_details;
    }

    @Override
    protected void init() {
        hasExtras();
        setData();
        initView();
    }

    private void hasExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            role = bundle.getInt(EXTRAS_ROLE, -1);
        }
        if (role == -1) {
            finish();
            return;
        }
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityPopularizeDetailsBinding) viewDataBinding;
        setTitle();
        setFragment();
    }

    private void setData() {
        labels[0] = "佣金记录";
        labels[1] = roleTitles[role - 1] + "列表";
    }

    private void setTitle() {
        // 设置标题
        mBinding.titleView.setTitleText("推荐" + roleTitles[role - 1]);
        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
    }

    private void setFragment() {
        fragments.add(IncomeRecordFragment.newInstance());
        fragments.add(IncomeRoleFragment.newInstance());
        mBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return labels[position];
            }
        });
        mBinding.tableLayout.setupWithViewPager(mBinding.viewPage);
        mBinding.viewPage.setOffscreenPageLimit(2);
        mBinding.viewPage.setCurrentItem(0, false);
    }
}
