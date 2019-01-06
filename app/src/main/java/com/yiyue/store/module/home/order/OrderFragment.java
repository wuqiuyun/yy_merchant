package com.yiyue.store.module.home.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BasePageMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentOrderBinding;
import com.yiyue.store.model.constant.OrderStatus;
import com.yiyue.store.model.vo.bean.OrderBean;
import com.yiyue.store.module.home.order.details.OrderDetailsActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.RefreshLayoutUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import static com.yiyue.store.model.constant.Constants.EXTRA_ORDER_TYPE;

/**
 * 我的订单
 * <p>
 * Created by zm on 2018/9/20.
 */
@CreatePresenter(OrderPresenter.class)
public class OrderFragment extends BasePageMvpFragment<IOrderView, OrderPresenter, OrderBean> implements IOrderView{
    @OrderStatus.OrderType
    private int orderType;
    private OrderAdapter orderAdapter;

    private RefreshLayout refreshLayout;
    private FragmentOrderBinding mBinding;

    /**
     *
     * @param orderType 订单类型
     * @return
     */
    public static Fragment newInstance(@OrderStatus.OrderType int orderType) {
        Fragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ORDER_TYPE, orderType);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        hasExtras();
        // 初始化加载刷新控件
        initRefreshLoadLayout();
        initRecycleView();
    }

    private void hasExtras() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderType = bundle.getInt(EXTRA_ORDER_TYPE);
        }
    }

    private void initRecycleView() {
        mBinding = (FragmentOrderBinding) viewDataBinding;
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.shap_divider_line)));
        mBinding.recycleView.addItemDecoration(divider);
        orderAdapter = new OrderAdapter(getContext(), orderType);
        orderAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                OrderDetailsActivity.startActivity(getContext(), orderAdapter.getDatas().get(position).getId());
            }
        });
        mBinding.recycleView.setAdapter(orderAdapter);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        pageIndx = 1;
        refreshLayout = null;
        getMvpPresenter().getOrderList(orderType+1, pageIndx, pageSize);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.NewMessage newMessage) {
        this.refreshLayout = null;
        getMvpPresenter().getOrderList(orderType+1, 1, orderAdapter.getDatas().size() == 0 ? pageSize : orderAdapter.getDatas().size());
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        pageIndx ++;
        this.refreshLayout = refreshLayout;
        getMvpPresenter().getOrderList(orderType+1, pageIndx, pageSize);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        pageIndx = 1;
        this.refreshLayout = refreshLayout;
        getMvpPresenter().getOrderList(orderType+1, pageIndx, pageSize);
    }

    @Override
    public void onGetOrderListSuccess(ArrayList<OrderBean> orderBeans) {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        if (refreshLayout == null || refreshLayout.getState() == RefreshState.Refreshing) {
            orderAdapter.setDatas(orderBeans, true);
        }else if (refreshLayout.getState() ==  RefreshState.Loading) {
            orderAdapter.addDatas(orderBeans, true);
        }

        if (orderBeans.size() == 0 && pageIndx == 1){
            mBinding.ivNoDate.setVisibility(View.VISIBLE);
        }else {
            mBinding.ivNoDate.setVisibility(View.GONE);
        }

    }

    @Override
    public void onGetOrderListFail() {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
    }
}
