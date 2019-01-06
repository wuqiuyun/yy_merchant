package com.yiyue.store.module.home.orders.manager;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.AdapterOrderManagerBinding;
import com.yiyue.store.model.vo.bean.OrderManagerBean;
import com.yiyue.store.util.FormatUtil;

/**
 * Created by zm on 2018/11/12.
 */
public class OrderManagerAdapter extends BaseRecycleViewAdapter<OrderManagerBean> {
    private LayoutInflater mInflater;
    private Context mContext;

    public OrderManagerAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderManagerViewHolder(mInflater.inflate(R.layout.adapter_order_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderManagerViewHolder viewHolder = (OrderManagerViewHolder) holder;

        OrderManagerBean bean = mDatas.get(position);

        // 用户名
        viewHolder.mBinding.tvUserName.setText(FormatUtil.Formatstring(bean.getUsernickname()));
        // 门店名
        viewHolder.mBinding.tvStoreName.setText(FormatUtil.Formatstring(bean.getStylistname()));
        // 预约时间段
        viewHolder.mBinding.tvTimeSpace.setText(FormatUtil.Formatstring(bean.getTime()));
    }

    private class OrderManagerViewHolder extends BaseViewHolder {
        AdapterOrderManagerBinding mBinding;

        public OrderManagerViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
