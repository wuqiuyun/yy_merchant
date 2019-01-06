package com.yiyue.store.module.common.addimg;

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
import com.yiyue.store.databinding.ItemImageAddBinding;
import com.yiyue.store.util.BitmapUtils;

/**
 * Created by zm on 2018/10/23.
 */
public class AddImageAdapter extends BaseRecycleViewAdapter<String>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnDeleteItemListener mOnDeleteItemListener;

    public AddImageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
        mOnDeleteItemListener = onDeleteItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddImageViewHolder(mLayoutInflater.inflate(R.layout.item_image_add, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AddImageViewHolder viewHolder = (AddImageViewHolder) holder;
        ImageView ivPhoto = viewHolder.mBinding.ivPhoto;

        String imagePath = mDatas.get(position);
        if (imagePath.startsWith("http")) {
            ImageLoader.loadImage(ivPhoto, imagePath);
        } else {
            ivPhoto.post(() -> {
                ivPhoto.setImageBitmap(BitmapUtils.decodeSampledBitmapFromFile(imagePath,
                        ivPhoto.getWidth(), ivPhoto.getHeight()));
            });
        }

        viewHolder.mBinding.ivDelete.setOnClickListener(v -> {
            if (mOnDeleteItemListener != null) {
                mOnDeleteItemListener.onDeleteItem(v, position);
            }
        });
    }

    private class AddImageViewHolder extends BaseViewHolder {
        private ItemImageAddBinding mBinding;

        public AddImageViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    public static interface OnDeleteItemListener {
        void onDeleteItem(View view, int position);
    }
}
