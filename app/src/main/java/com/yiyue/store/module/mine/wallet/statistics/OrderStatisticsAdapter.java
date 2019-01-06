package com.yiyue.store.module.mine.wallet.statistics;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.databinding.ItemOrderManagerBinding;
import com.yiyue.store.model.vo.bean.StoreOrderBean;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;


/*
 *  @项目名：  my_merchant 
 *  @包名：    com.yiyue.store.module.home.orders
 *  @文件名:   OrderStatisticsAdapter
 *  @创建者:   27407
 *  @创建时间:  2018/10/14 14:06
 *  @描述：    订单统计适配器
 */

public class OrderStatisticsAdapter extends BaseRecycleViewAdapter<StoreOrderBean.OrderCategoryCountBean> {
    private Context context;

    public OrderStatisticsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderStatisticsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_manager, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderStatisticsViewHolder viewHolder = (OrderStatisticsViewHolder) holder;
        StoreOrderBean.OrderCategoryCountBean bean = getDatas().get(position);
        viewHolder.mBinding.tvServiceName.setText(bean.getName());
        viewHolder.mBinding.tvOrderReservation.setText(String.valueOf(bean.getReceiptNum()));
        viewHolder.mBinding.tvOrderFinish.setText(String.valueOf(bean.getSuccessNum()));
        viewHolder.mBinding.tvIncomeEstimate.setText(String.valueOf(bean.getReceiptMoney()));
        viewHolder.mBinding.tvIncomeTotal.setText(String.valueOf(bean.getSuccessMoney()));
    }

    private class OrderStatisticsViewHolder extends BaseViewHolder {
        ItemOrderManagerBinding mBinding;

        public OrderStatisticsViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}