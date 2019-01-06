package com.yiyue.store.module.home.orders.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentOrderManagerBinding;
import com.yiyue.store.model.vo.bean.OrderManagerBean;
import com.yiyue.store.module.home.order.details.OrderDetailsActivity;
import com.yiyue.store.util.FormatUtil;

import java.util.ArrayList;

/**2
 * 时间管理
 * <p>
 * Created by zm on 2018/11/12.
 */
@CreatePresenter(OrderManagerPresenter.class)
public class OrderManagerFragment extends BaseMvpFragment<IOrderManagerView, OrderManagerPresenter> implements IOrderManagerView{
    private static final String ARGUMENTS_DATE = "date";

    FragmentOrderManagerBinding mBinding;
    private OrderManagerAdapter mAdapter;
    private String date;

    public static OrderManagerFragment newInstance(String date) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENTS_DATE, date);
        OrderManagerFragment fragment = new OrderManagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            date = bundle.getString(ARGUMENTS_DATE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_manager;
    }

    @Override
    protected void initView() {
        mBinding = (FragmentOrderManagerBinding) viewDataBinding;
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderManagerAdapter(getContext());
        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                OrderDetailsActivity.startActivity(getContext(), mAdapter.getDatas().get(position).getOrderId());
            }
        });
        mBinding.recycleView.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        getMvpPresenter().getOrderDatas(date);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void setDatas(ArrayList<OrderManagerBean> datas) {
        mAdapter.setDatas(datas, true);
    }
}
