package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.income;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentIncomeBinding;
import com.yiyue.store.model.vo.bean.MonthSumBean;
import com.yiyue.store.module.home.invite.IUpDataFragment;
import com.yiyue.store.module.mine.wallet.bill.BillActivity;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.MonthBillActivity;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 总收入
 */
@CreatePresenter(IncomePresenter.class)
public class IncomeFragment extends BaseMvpFragment<IIncomeView, IncomePresenter> implements IIncomeView, ViewPager.OnPageChangeListener ,ClickHandler,IUpDataFragment {

    private List<Fragment> mFragments;
    private FragmentIncomeBinding mBinding;
    private Bundle mBundle;
    private String date= DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM);
    public boolean selectMonth=false;


    public static IncomeFragment newInstance() {
        IncomeFragment incomeFragment = new IncomeFragment();
        Bundle bundle = new Bundle();
        incomeFragment.setArguments(bundle);
        return incomeFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_income;
    }
    public void setOrders(MonthSumBean monthSumInBean){
        mBinding.tvOrders.setText(String.format(getString(R.string.orders),String.valueOf(monthSumInBean.getBits())));
        mBinding.tvTotalIncome.setText(String.format(String.valueOf(monthSumInBean.getInSum())));
    }
    @Override
    protected void initView() {
        hasExtras();
        mBinding = (FragmentIncomeBinding) viewDataBinding;
        mBinding.setClick(this);
        setFragment();
        MonthBillActivity activity = (MonthBillActivity) getActivity();
        activity.setIUpIncomeFragment(this);
        mBinding.rgFiltrate.setOnCheckedChangeListener((radioGroup,checkedId)-> {
            switch (checkedId) {
                case R.id.tab1:
                    mBinding.viewPage.setCurrentItem(0, false);
                    activity.hideDateTextView(View.VISIBLE);
                    selectMonth=false;
                    break;
                case R.id.tab2:
                    mBinding.viewPage.setCurrentItem(1, false);
                    activity.hideDateTextView(View.GONE);
                    selectMonth=true;
                    break;
            }
        });
        mBinding.viewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mBinding.viewPage.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void hasExtras() {
        mBundle = getArguments();
        if (mBundle!=null){
        }
    }


    @Override
    protected void loadData() {
//        getMvpPresenter().getOrderList(orderType+1, pageIndx, pageSize);
    }
    private void setFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(IncomeChartPieFragment.newInstance(IncomeFragment.this));
        mFragments.add(new IncomeChartBarFragment());
        mBinding.viewPage.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        mBinding.viewPage.addOnPageChangeListener(this);
//        // 是否允许viewPage滑动切换
        mBinding.viewPage.setScanScroll(false);
        // viewPage预加载1个页面
        mBinding.viewPage.setOffscreenPageLimit(1);
        // 加载第几个页面
        mBinding.viewPage.setCurrentItem(0, false);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBinding.rgFiltrate.check(R.id.tab1);
                break;
            case 1:
                mBinding.rgFiltrate.check(R.id.tab2);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void getMonthSumIn(MonthSumBean monthSumInBean) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_total_income:
                Bundle bundle = new Bundle();
                bundle.putString("month", date);
                bundle.putInt("billType", 1);
                BillActivity.startActivity(getContext(), BillActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onUpData(int filterType, Object o) {
        date=String.valueOf(o);
    }
}
