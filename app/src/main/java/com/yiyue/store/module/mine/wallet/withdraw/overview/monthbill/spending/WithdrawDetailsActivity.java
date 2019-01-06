package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityWithdrawDetailsBinding;
import com.yiyue.store.model.vo.bean.BankSumBean;
import com.yiyue.store.module.mine.wallet.bill.card.CardDetailsActivity;
import com.yiyue.store.widget.mytimepickview.CustomDatePicker;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;


import java.util.ArrayList;
import java.util.Date;

/**
 * 提现账单-概览
 */
@CreatePresenter(WithdrawDetailsPresenter.class)
public class WithdrawDetailsActivity extends BaseMvpAppCompatActivity<IWithdrawDetailsView, WithdrawDetailsPresenter>
        implements IWithdrawDetailsView, ClickHandler {

    private ActivityWithdrawDetailsBinding mBinding;
    private CustomDatePicker mDatePicker;
    private String searchDate;
    private WithdrawToBankAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraw_details;
    }

    @Override
    protected void init() {

        initView();

        String date = mBinding.tvDate.getText().toString();
        date = date.replace("年", "-");
        date = date.replace("月", "-");
        date += "01";
        searchDate = date;
        getMvpPresenter().getBankSum(searchDate);
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityWithdrawDetailsBinding) viewDataBinding;
        mBinding.setClick(this);
        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
        // recycleview
        mBinding.rvBank.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WithdrawToBankAdapter(this);
        mBinding.rvBank.setAdapter(adapter);
        //初始化日期选择
        initDatePicker();
        mBinding.tvDate.setText(DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM_CN));
        adapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                BankSumBean bankSumBean = adapter.getDatas().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bankSumBean", bankSumBean);
                bundle.putString("date", mBinding.tvDate.getText().toString());
                CardDetailsActivity.startActivity(WithdrawDetailsActivity.this, CardDetailsActivity.class, bundle);
            }
        });
    }

    private void initDatePicker() {
        String mNowDate = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YMD);
        //刷新数据
        mDatePicker = new CustomDatePicker(this, "2018-01-01", mNowDate, time -> {
            String[] split = time.split("-");
            mBinding.tvDate.setText(split[0] + "年" + split[1] + "月");
            //刷新数据
            searchDate = split[0] + "-" + split[1];
            getMvpPresenter().getBankSum(searchDate);
        });
        //隐藏Day
        mDatePicker.setHideDay(true);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                String date = mBinding.tvDate.getText().toString();
                date = date.replace("年", "-");
                date = date.replace("月", "-");
                date += "01";
                mDatePicker.show(date);
                break;
        }
    }

    @Override
    public void getBankSumSuccess(ArrayList<BankSumBean> beans) {
        adapter.setDatas(beans, true);
        if (adapter.getDatas().size() == 0) {
            mBinding.noData.setVisibility(View.VISIBLE);
        } else {
            mBinding.noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void getBankSumFail() {

    }
}
