package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill;

import android.content.Intent;
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
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.databinding.ActivityMonthBillBinding;
import com.yiyue.store.module.home.invite.IUpDataFragment;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.income.IncomeFragment;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending.SpendingFragment;
import com.yiyue.store.widget.mytimepickview.CustomDatePicker;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * 收入明细-概览
 */
public class MonthBillActivity extends BaseAppCompatActivity
        implements ClickHandler {
    private ActivityMonthBillBinding mBinding;
    private String today = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM_CN);
    private final String[] labels = new String[]{
            YLApplication.getContext().getString(R.string.income),
            YLApplication.getContext().getString(R.string.spending)
    };
    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private IUpDataFragment iUpSpending;
    private IUpDataFragment iUpIncome;
    private IUpDataFragment iUpChartPie;
    private IUpDataFragment iUpChartBar;
    private IncomeFragment mIncomeFragment;

    public void setIUpSpendingFragment(IUpDataFragment iUpDataFragment) {
        iUpSpending = iUpDataFragment;
    }
    public void setIUpIncomeFragment(IUpDataFragment iUpDataFragment) {
        iUpIncome = iUpDataFragment;
    }
    public void setIUpChartPieFragment(IUpDataFragment iUpDataFragment) {
        iUpChartPie = iUpDataFragment;
    }
    public void setIUpChartBarFragment(IUpDataFragment iUpDataFragment) {
        iUpChartBar = iUpDataFragment;
    }

    private CustomDatePicker mDatePicker;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_month_bill;
    }

    @Override
    protected void init() {
        initView();
    }

    public void hideDateTextView(int visibility){
            mBinding.tvDate.setVisibility(visibility);
    }
    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityMonthBillBinding) viewDataBinding;
        mBinding.setClick(this);
        setTable(mBinding.tableLayout, getLayoutInflater());
        mIncomeFragment = IncomeFragment.newInstance();
        fragments.add(mIncomeFragment);
        fragments.add(SpendingFragment.newInstance());
        mBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
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
                if (tab.getPosition()==1){
                    mBinding.tvDate.setVisibility(View.VISIBLE);
                }else if (mIncomeFragment.selectMonth){
                    mBinding.tvDate.setVisibility(View.GONE);
                }
                mBinding.viewPage.setCurrentItem(tab.getPosition(), false);
            }
        });
        // 是否允许viewPage滑动切换
        mBinding.viewPage.setScanScroll(false);
        // viewPage预加载1个页面
        mBinding.viewPage.setOffscreenPageLimit(1);
        // 加载第几个页面
        mBinding.viewPage.setCurrentItem(0, false);
        //初始化日期选择
        initDatePicker();
        mBinding.tvDate.setText(today);
    }
    private void initDatePicker() {
       String mNowDate = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        mDatePicker = new CustomDatePicker(this, "2018-01-01", mNowDate, time -> {
            String[] date = time.split("-");
            mBinding.tvDate.setText(date[0]+"年"+date[1]+"月");
            //刷新数据
            upFragmentData(date);
        });
        //隐藏Day
        mDatePicker.setHideDay(true);
    }

    /**
     * 刷新fragmnet数据
     * @param date
     */
    private void upFragmentData(String[] date) {
        if (iUpSpending!=null){
            iUpSpending.onUpData(0,date[0]+"-"+date[1]);
        }
        if (iUpIncome!=null){
            iUpIncome.onUpData(0,date[0]+"-"+date[1]);
        }
        if (iUpChartPie!=null){
            iUpChartPie.onUpData(0,date[0]+"-"+date[1]);
        }
        if (iUpChartBar!=null){
            iUpChartBar.onUpData(0,date[0]+"-"+date[1]);
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0,null);
    }
    /**
     * 设置Tab
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_date:
                String date = mBinding.tvDate.getText().toString();
                date=date.replace("年", "-");
                date=date.replace("月", "-");
                date+="01";
                mDatePicker.show(date);
                break;
        }
    }
}
