package com.yiyue.store.module.mine.stylist.details;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yl.core.component.image.ImageLoader;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemServiceBundleBinding;
import com.yiyue.store.model.vo.bean.StylistCardBean;

/**
 * 套餐项目适配器
 * Created by zm on 2018/10/11.
 */
public class ServiceBundleAdapter extends BaseRecycleViewAdapter<StylistCardBean.CardPackages>{
    private Context context;

    public ServiceBundleAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceProjectViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_service_bundle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ServiceBundleAdapter.ServiceProjectViewHolder viewHolder = (ServiceBundleAdapter.ServiceProjectViewHolder) holder;
        ImageView circleImageView = viewHolder.mBinding.ivBundleImg;
        ImageLoader.loadImage(circleImageView,mDatas.get(position).getPrice());
        viewHolder.mBinding.tvBundleName.setText(mDatas.get(position).getName());
        viewHolder.mBinding.tvProjectPrice.setText("￥"+mDatas.get(position).getPrice());
    }

    private class ServiceProjectViewHolder extends BaseViewHolder {
        private ItemServiceBundleBinding mBinding;

        public ServiceProjectViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
