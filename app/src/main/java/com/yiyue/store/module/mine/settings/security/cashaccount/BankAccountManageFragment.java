package com.yiyue.store.module.mine.settings.security.cashaccount;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yiyue.store.model.vo.bean.StoreAuthBean;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentCashaccountBankBinding;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.model.vo.bean.ServerItemBean;
import com.yiyue.store.module.mine.settings.security.cashaccount.addbankcard.AddBankCardActivity;
import com.yiyue.store.module.mine.settings.security.cashaccount.unbankcard.UnBankCardActivity;

import java.util.ArrayList;

/**
 * 银行卡提现账户
 * Created by lyj on 2018/11/8
 */
@CreatePresenter(CashAccountManagePresenter.class)
public class BankAccountManageFragment extends BaseMvpFragment<CashAccountManageView,CashAccountManagePresenter>
        implements ClickHandler, CashAccountManageView {

    private FragmentCashaccountBankBinding mBinding;
    private int mFrom;
    private CashAccountManageAdapter mAdapter;
    private ArrayList<CashAliBean> cashBankList = new ArrayList<>();


    public static Fragment newInstance(int from) {
        BankAccountManageFragment bankAccountManageFragment = new BankAccountManageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("from",from);
        bankAccountManageFragment.setArguments(bundle);
        return bankAccountManageFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cashaccount_bank;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        mFrom = bundle.getInt("from", 0);
        mBinding = (FragmentCashaccountBankBinding) viewDataBinding;
        mBinding.setClick(this);
        // init recycleview
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recycleView.addItemDecoration(new GridSpacingItemDecoration(1, 30, false));
        mBinding.recycleView.setNestedScrollingEnabled(false);
//        mAdapter = new CashAccountManageAdapter(getContext());
//        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
//            @Override
//            public void onItemClick(View view, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("CashAliBean",cashBankList.get(position));
//                UnBankCardActivity.startActivity(getContext(), UnBankCardActivity.class, bundle);
//            }
//        });
//        mBinding.recycleView.setAdapter(mAdapter);

    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ll_bankcard_add ://添加银行卡
            case R.id.bt_bankcard_add ://添加银行卡
                AddBankCardActivity.startActivity(getContext() , AddBankCardActivity.class);
                break;
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onextractaLIAccountSuccess(ArrayList<CashAliBean> cashAliBean) {

    }

    @Override
    public void onextractBankAccountSuccess(ArrayList<CashAliBean> cashAliBean) {
//        if (cashAliBean!=null&&cashAliBean.size()>0){
//            cashBankList.clear();
//            cashBankList = cashAliBean;
//            mAdapter.getDatas().clear();
//            mAdapter.getDatas().addAll(cashBankList);
//            mAdapter.notifyDataSetChanged();
//        }else {
//            mAdapter.getDatas().clear();
//            mAdapter.notifyDataSetChanged();
//        }

    }

    @Override
    public void onUnBindSuccess() {

    }

    @Override
    public void onSuccess(ArrayList<ServerItemBean> serverItemBeans) {
    }

    @Override
    public void onFail(int type) {

    }

    @Override
    public void getUserInfoSuccess(StoreAuthBean storeAuthBean) {

    }

    @Override
    public void getUserInfoFail() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getMvpPresenter().extractBankAccount("BANK",getActivity());
    }

}
