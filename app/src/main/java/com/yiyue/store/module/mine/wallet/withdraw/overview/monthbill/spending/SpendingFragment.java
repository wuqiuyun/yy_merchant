package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentSpendingBinding;
import com.yiyue.store.model.vo.bean.MonthSumBean;
import com.yiyue.store.module.home.invite.IUpDataFragment;
import com.yiyue.store.module.mine.wallet.bill.BillActivity;
import com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.MonthBillActivity;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * 总支出
 */
@CreatePresenter(SpendingPresenter.class)
public class SpendingFragment extends BaseMvpFragment<ISpendingView, SpendingPresenter> implements ISpendingView, IUpDataFragment, ClickHandler {
    private FragmentSpendingBinding mBinding;
    private SpendingAdapter mAdapter;
    private String date = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM);

    public static Fragment newInstance() {
        Fragment incomeFragment = new SpendingFragment();
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
        return R.layout.fragment_spending;
    }

    @Override
    protected void initView() {
        hasExtras();
        mBinding = (FragmentSpendingBinding) viewDataBinding;
        MonthBillActivity activity = (MonthBillActivity) getActivity();
        activity.setIUpSpendingFragment(this);
        mBinding.setClick(this);
        // recycleview
        mBinding.rvSpending.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SpendingAdapter(getContext());
        mBinding.rvSpending.setAdapter(mAdapter);
        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("month", date);
//                bundle1.putInt("billType", 2);
//                BillActivity.startActivity(getContext(), BillActivity.class, bundle1);
            }
        });
    }

    private void hasExtras() {
        Bundle bundle = getArguments();
    }


    @Override
    protected void loadData() {
        getMvpPresenter().getMonthSumOut(DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM));
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }


    @Override
    public void onUpData(int filterType, Object o) {
        date = String.valueOf(o);
        getMvpPresenter().getMonthSumOut(date);
    }

    @Override
    public void getMonthSumOut(MonthSumBean monthSumBean) {
        if (monthSumBean.getClassifyOut()==null){
            mAdapter.getDatas().clear();
            mAdapter.notifyDataSetChanged();
        }else {
            mAdapter.setDatas((ArrayList<MonthSumBean.ClassifyOutBean>) monthSumBean.getClassifyOut(), true);
        }
        mBinding.tvTotalSpending.setText(String.valueOf(monthSumBean.getOutSum()));
        mBinding.tvOrders.setText(String.format(getString(R.string.orders), String.valueOf(monthSumBean.getBits())));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_total_spending:
                Bundle bundle1 = new Bundle();
                bundle1.putString("month", date);
                bundle1.putInt("billType", 2);
                BillActivity.startActivity(getContext(), BillActivity.class, bundle1);
                break;
        }
    }
}
