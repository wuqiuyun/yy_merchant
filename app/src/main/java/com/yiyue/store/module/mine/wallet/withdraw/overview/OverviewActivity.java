package com.yiyue.store.module.mine.wallet.withdraw.overview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityOverviewBinding;
import com.yiyue.store.model.vo.bean.BankSumBean;
import com.yiyue.store.model.vo.bean.MonthSumBean;
import com.yiyue.store.module.mine.wallet.bill.BillActivity;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.MonthBillActivity;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending.WithdrawDetailsActivity;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending.WithdrawToBankAdapter;
import com.yiyue.store.util.PieChartManager;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;


/**
 * 收入明细-概览
 */
@CreatePresenter(OverviewPresenter.class)
public class OverviewActivity extends BaseMvpAppCompatActivity<IOverviewView, OverviewPresenter> implements IOverviewView, ClickHandler {
    private ActivityOverviewBinding mBinding;
    private WithdrawToBankAdapter adapter;
    private String searchMonth;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_overview;
    }

    @Override
    protected void init() {
        initView();
        thisMonth();
        loadData();
    }

    private void loadData() {
        getMvpPresenter().getNewesTwo(searchMonth);
        getMvpPresenter().getMonthSum(searchMonth);
    }


    @SuppressLint("StringFormatInvalid")
    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityOverviewBinding) viewDataBinding;
        mBinding.setClick(this);
//        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.titleView.setRightTextClickListener(v -> {
        });
        // recycleview
        mBinding.includeBank.rvBank.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WithdrawToBankAdapter(this);
        mBinding.includeBank.rvBank.setAdapter(adapter);
        setOnClickListener();
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    private void setOnClickListener() {
        mBinding.includeBill.tvBillDetils.setOnClickListener(view -> {
            MonthBillActivity.startActivity(this, MonthBillActivity.class);
        });
        //提现账单
        mBinding.includeBank.tvBankDetils.setOnClickListener(view -> {
            WithdrawDetailsActivity.startActivity(this, WithdrawDetailsActivity.class);
        });
        //总收入账单
        mBinding.includeBill.llIncome.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("month", searchMonth);
            bundle.putInt("billType", 1);
            BillActivity.startActivity(this, BillActivity.class, bundle);
        });
        //总支出账单
        mBinding.includeBill.llExpend.setOnClickListener(view -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("month", searchMonth);
            bundle1.putInt("billType", 2);
            BillActivity.startActivity(this, BillActivity.class, bundle1);
        });

    }

    @Override
    public void getNewesTwoSuccess(ArrayList<BankSumBean> beans) {
        if (beans == null) return;
        if (beans.size() == 0) {
            mBinding.includeBank.vNodata.setVisibility(View.VISIBLE);
        } else {
            mBinding.includeBank.vNodata.setVisibility(View.GONE);
        }

        adapter.setDatas(beans, true);
    }

    private void thisMonth() {
        String time = DateUtil.getCurDateStr();
        searchMonth = String.valueOf(time.subSequence(0, 7));
        mBinding.includeBill.tvBillMonth.setText(String.format(getString(R.string.bill_month),time.split("-")[1]));
        mBinding.includeBank.tvWithdrawalMonth.setText(String.format(getString(R.string.withdrawal_month),time.split("-")[1]));
    }

    @Override
    public void getMonthSum(MonthSumBean monthSumBean) {
        mBinding.includeBill.tvTotalIncome.setText(String.valueOf(monthSumBean.getInSum()));
        mBinding.includeBill.tvTotalSpending.setText(String.valueOf(monthSumBean.getOutSum()));
        if (monthSumBean.getMaxFly()!=null){
            mBinding.includeBill.tvServiceIncome.setText(String.valueOf(monthSumBean.getMaxFly().getInMoney()));
            mBinding.includeBill.tvTypeName.setText(String.valueOf(monthSumBean.getMaxFly().getName()));
        }else {
            mBinding.includeBill.llTypeName.setVisibility(View.GONE);
        }

        if (monthSumBean.getClassifyIn()==null||monthSumBean.getClassifyIn().size()==0)return;
        mBinding.includeBill.pieChart.upData(monthSumBean.getClassifyIn());
    }
}
