package com.yiyue.store.module.mine.stylist.details;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.util.FormatKmUtil;
import com.yl.core.component.image.ImageLoader;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemStoreJoinBinding;
import com.yiyue.store.model.vo.bean.StylistCardBean;

/**
 * Created by zm on 2018/10/11.
 */
public class StoreAdapter extends BaseRecycleViewAdapter<StylistCardBean.CardStoreDTOsBean>{
    private Context context;

    public StoreAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoreViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_join, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StoreAdapter.StoreViewHolder viewHolder = (StoreAdapter.StoreViewHolder) holder;
        viewHolder.binding.tvName.setText(mDatas.get(position).getStorename());
        viewHolder.binding.tvAddress.setText(mDatas.get(position).getLocation());
        viewHolder.binding.tvLocation.setText(FormatKmUtil.FormatKmStr(mDatas.get(position).getDistance()));
        ImageLoader.loadImage(viewHolder.binding.ivStore,mDatas.get(position).getPicture());
    }

    private class StoreViewHolder extends BaseViewHolder {
        private ItemStoreJoinBinding binding;
        public StoreViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
