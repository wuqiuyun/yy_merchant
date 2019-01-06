package com.yiyue.store.module.mine.wallet.bill;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityIncomeBillBinding;
import com.yiyue.store.model.vo.bean.TotalBillBean;
import com.yiyue.store.module.mine.wallet.bill.details.BillDetailsActivity;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;

/**
 * 总收入/支出账单
 * Created by wqy on 2018/12/18.
 */
@CreatePresenter(BillPresenter.class)
public class BillActivity extends BaseMvpAppCompatActivity<IBillView, BillPresenter>
        implements IBillView, OnRefreshListener, OnLoadMoreListener {
    private static final int BILL_EXPEND = 1;// 总支出账单
    private static final int BILL_INCOME = 2;// 总收入账单
    private ActivityIncomeBillBinding mBinding;
    private BillAdapter mAdapter;
    private RefreshLayout mRefreshLayout;
    private int billType = BILL_INCOME;
    private String month;
    private int page = 1;
    private int size = 10;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_income_bill;
    }

    @Override
    protected void init() {
        initView();
        hasExtras();
        loadData();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityIncomeBillBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());

        mAdapter = new BillAdapter(BillActivity.this);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(BillActivity.this));
        mBinding.recycleView.setAdapter(mAdapter);

        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                TotalBillBean bean = mAdapter.getDatas().get(position);
                Bundle bundle = new Bundle();
                bundle.putString("billId", bean.getBillId());
                bundle.putInt("inType", bean.getInType());
                bundle.putInt("type", billType);
                BillDetailsActivity.startActivity(BillActivity.this, BillDetailsActivity.class, bundle);
            }
        });
        initRefreshLoadLayout();
    }

    protected void initRefreshLoadLayout() {
        mRefreshLayout = mBinding.refreshLayout;
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
            mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
            mRefreshLayout.setOnLoadMoreListener(this);
            mRefreshLayout.setOnRefreshListener(this);
        }
    }

    private void hasExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            billType = bundle.getInt("billType", 1);
            month = bundle.getString("month");
        }
        if (billType == 1) {
            mBinding.titleView.setTitleText("总收入账单");
        } else {
            mBinding.titleView.setTitleText("总支出账单");
        }
        mAdapter.getType(billType);
    }

    private void loadData() {
        getMvpPresenter().getBill(month, page, size, billType);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        ++page;
        loadData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        loadData();
    }

    @Override
    public void getBillSuccess(ArrayList<TotalBillBean> list) {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
        if (null != list && list.size() > 0) {
            if (page == 1) {
                mAdapter.setDatas(list, true);
            } else {
                mAdapter.addDatas(list, true);
            }

            if (list.size() < size) {// 加载的数据不够页面数量 则认为没有下一页
                mRefreshLayout.setNoMoreData(true);
            } else {
                mRefreshLayout.setNoMoreData(false);
            }
            mBinding.noData.setVisibility(View.GONE);
        } else {
            mBinding.noData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getBillFail() {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
    }
}
