package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.income;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentChartPieBinding;
import com.yiyue.store.model.vo.bean.MonthSumBean;
import com.yiyue.store.module.home.invite.IUpDataFragment;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.MonthBillActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PieChartManager;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;


import java.util.ArrayList;
import java.util.Date;


/**
 * 总收入-饼图
 */
@CreatePresenter(IncomePresenter.class)
public class IncomeChartPieFragment extends BaseMvpFragment<IIncomeView, IncomePresenter> implements IIncomeView,IUpDataFragment {

    private FragmentChartPieBinding mBinding;
    private Bundle mBundle;
    public static IncomeFragment mIncomeFragment;

    public static Fragment newInstance(IncomeFragment incomeFragment) {
        Fragment incomeChartPieFragment = new IncomeChartPieFragment();
        Bundle bundle = new Bundle();
        incomeChartPieFragment.setArguments(bundle);
        mIncomeFragment=incomeFragment;
        return  incomeChartPieFragment;
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
        return R.layout.fragment_chart_pie;
    }

    @Override
    protected void initView() {
        mBinding = (FragmentChartPieBinding) viewDataBinding;
        hasExtras();
        MonthBillActivity activity = (MonthBillActivity) getActivity();
        activity.setIUpChartPieFragment(this);
    }

    private void hasExtras() {
        mBundle = getArguments();
    }


    @Override
    protected void loadData() {
            getMvpPresenter().getMonthSumIn(DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM),"1");
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }


    @Override
    public void getMonthSumIn(MonthSumBean monthSumInBean) {
        if (mIncomeFragment!=null)mIncomeFragment.setOrders(monthSumInBean);
        if (monthSumInBean.getClassifyIn()==null||monthSumInBean.getClassifyIn().size()==0){
            mBinding.pieChart.clear();
        }else {
            mBinding.pieChart.upData(monthSumInBean.getClassifyIn());
        }
    }

    @Override
    public void onUpData(int filterType, Object o) {
        getMvpPresenter().getMonthSumIn((String) o,"1");
    }
}
