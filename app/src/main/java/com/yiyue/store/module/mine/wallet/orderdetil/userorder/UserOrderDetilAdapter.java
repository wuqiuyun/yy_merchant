package com.yiyue.store.module.mine.wallet.orderdetil.userorder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemUserOrderDetilBinding;
import com.yiyue.store.databinding.ItemWalletOrderBinding;
import com.yiyue.store.model.vo.bean.OrderDetailBean;
import com.yiyue.store.model.vo.bean.UserOrderBean;
import com.yl.core.component.image.ImageLoader;


/**
 * Created by zm on 2018/10/8.
 */
public class UserOrderDetilAdapter extends BaseRecycleViewAdapter<UserOrderBean> {
    private Context context;

    public UserOrderDetilAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletOrderHolder(LayoutInflater.from(context).inflate(R.layout.item_user_order_detil, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserOrderDetilAdapter.WalletOrderHolder viewHolder = (UserOrderDetilAdapter.WalletOrderHolder) holder;
        UserOrderBean userOrderBean = mDatas.get(position);
        ImageLoader.loadImage(viewHolder.mBinding.ivPhoto,userOrderBean.getUserPhotoPath());
        viewHolder.mBinding.tvOrderAmount.setText(String.format(context.getString(R.string.RMB2),userOrderBean.getAmount()));
        viewHolder.mBinding.tvName.setText(String.format(userOrderBean.getNickName()));
        viewHolder.mBinding.tvProject.setText(String.format(userOrderBean.getServiceName()));
        viewHolder.mBinding.tvDate.setText(String.format(userOrderBean.getSuccessTime().split("\\s")[0]));

    }


    private class WalletOrderHolder extends BaseViewHolder {
        private ItemUserOrderDetilBinding mBinding;
        public WalletOrderHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
