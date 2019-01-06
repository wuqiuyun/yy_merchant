package com.yiyue.store.module.home.evaluation;

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
import com.yiyue.store.databinding.ItemPhotoBinding;


/*
 *  @创建者:   27407
 *  @创建时间:  2018/10/18 10:32
 *  @描述：    评价管理图片适配器
 */

class PhotoAdapter extends BaseRecycleViewAdapter<String> {

    private Context mContext;


    public PhotoAdapter(Context context) {
        mContext = context;

    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);

        return new PhotoAdapter.PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PhotoAdapter.PhotoViewHolder viewHolder = (PhotoAdapter.PhotoViewHolder) holder;
        ImageView circleImageView = viewHolder.mBinding.ivReply;
        ImageLoader.loadImage(circleImageView,mDatas.get(position));
    }


    public class PhotoViewHolder extends BaseViewHolder {
        private ItemPhotoBinding mBinding;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
