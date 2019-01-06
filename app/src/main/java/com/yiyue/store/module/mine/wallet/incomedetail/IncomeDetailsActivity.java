package com.yiyue.store.module.mine.wallet.incomedetail;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ActivityIncomeDetailsBinding;
import com.yiyue.store.module.mine.wallet.withdraw.overview.OverviewActivity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;

/**
 * 收入明细
 * Created by wqy on 2018/12/18.
 */
@CreatePresenter(IncomeDetailsPresenter.class)
public class IncomeDetailsActivity extends BaseAppCompatActivity {

    public static final String INCOME_BALANCE = "income_balance";//余额
    public static final String INCOME_SCORE = "income_score";//积分
    private String incomeType = INCOME_BALANCE;

    private ActivityIncomeDetailsBinding mBinding;
    private final String[] labels = new String[]{
            YLApplication.getContext().getString(R.string.balance),
            YLApplication.getContext().getString(R.string.score)
    };
    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_income_details;
    }

    @Override
    protected void init() {
        initView();
        setFragment();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityIncomeDetailsBinding) viewDataBinding;

        //titleView
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.titleView.setRightImgClickListener(view -> {
            //概览
            OverviewActivity.startActivity(this, OverviewActivity.class);
        });

        // TabLayout
        setTable(mBinding.tableLayout, getLayoutInflater());
    }

    private void setFragment() {
        mFragments.add(IncomeAssetFragment.newInstance());
        mFragments.add(IncomeCoinFragment.newInstance());

        mBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }
        });
        mBinding.viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tableLayout));
        mBinding.tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewPage) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPage.setCurrentItem(tab.getPosition(), false);
                if (tab.getPosition() == 0) {
                    mBinding.titleView.setRightIconVisib(true);
                } else {
                    mBinding.titleView.setRightIconVisib(false);
                }
            }
        });
        // 是否允许viewPage滑动切换
        mBinding.viewPage.setScanScroll(false);
        // viewPage预加载1个页面
        mBinding.viewPage.setOffscreenPageLimit(1);
        // 加载第几个页面
        mBinding.viewPage.setCurrentItem(0, false);
    }

    /**
     * 设置Tab
     *
     * @param tabLayout
     * @param inflater
     */
    private void setTable(TabLayout tabLayout, LayoutInflater inflater) {
        for (String label : labels) {
            TabLayout.Tab newTab = tabLayout.newTab();
            View tabView = inflater.inflate(R.layout.tab_layout_home, null);
            newTab.setCustomView(tabView);

            TextView tvLabel = tabView.findViewById(R.id.tv_label);
            tvLabel.setText(label);
            tabLayout.addTab(newTab);
        }
    }

}
