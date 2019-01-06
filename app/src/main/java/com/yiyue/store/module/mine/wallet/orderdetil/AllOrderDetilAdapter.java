package com.yiyue.store.module.mine.wallet.orderdetil;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemWalletOrderBinding;
import com.yiyue.store.model.vo.bean.OrderDetailBean;
import com.yl.core.component.image.ImageLoader;


/**
 * Created by zm on 2018/10/8.
 */
public class AllOrderDetilAdapter extends BaseRecycleViewAdapter<OrderDetailBean> {
    private Context context;

    public AllOrderDetilAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletOrderHolder(LayoutInflater.from(context).inflate(R.layout.item_wallet_order, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllOrderDetilAdapter.WalletOrderHolder viewHolder = (AllOrderDetilAdapter.WalletOrderHolder) holder;
        OrderDetailBean orderDetailBean = mDatas.get(position);

        ImageLoader.loadImage(viewHolder.mBinding.ivPhoto,orderDetailBean.getUserPhotoPath());
        viewHolder.mBinding.tvName.setText(String.format(orderDetailBean.getNickName()));
        viewHolder.mBinding.tvStore.setText(String.format(orderDetailBean.getStoreName()));
        viewHolder.mBinding.tvProject.setText(String.format(orderDetailBean.getOrderName()));
        viewHolder.mBinding.tvPayment.setText(String.valueOf(orderDetailBean.getPayAmount()));
        viewHolder.mBinding.tvIncome.setText(String.valueOf(orderDetailBean.getAmount()));
        if (orderDetailBean.getIsclear()==0){
            viewHolder.mBinding.tvTime.setText(orderDetailBean.getCreatetime().split("\\s")[0]);
            viewHolder.mBinding.tvOrderType.setText("已完成");
            viewHolder.mBinding.tvOrderType.setBackground(context.getResources().getDrawable(R.drawable.shape_order_red_gradient));
        }else {
            viewHolder.mBinding.tvTime.setText(orderDetailBean.getCleartime().split("\\s")[0]);
            viewHolder.mBinding.tvOrderType.setText("已结算");
            viewHolder.mBinding.tvOrderType.setBackground(context.getResources().getDrawable(R.drawable.shape_order_blue_gradient));
        }
    }

    private class WalletOrderHolder extends BaseViewHolder {
        private ItemWalletOrderBinding mBinding;
        public WalletOrderHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
