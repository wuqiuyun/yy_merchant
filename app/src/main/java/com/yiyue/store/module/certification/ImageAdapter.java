package com.yiyue.store.module.certification;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemImageStoreBinding;
import com.yiyue.store.util.BitmapUtils;
import com.yl.core.component.image.ImageLoader;

/**
 * Created by zm on 2018/10/23.
 */
public class ImageAdapter extends BaseRecycleViewAdapter<String>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_image_store, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        ImageView ivStore = viewHolder.mBinding.ivStore;
        ImageLoader.loadImage(ivStore, mDatas.get(position));
    }

    private class ImageViewHolder extends BaseViewHolder {
        private ItemImageStoreBinding mBinding;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
