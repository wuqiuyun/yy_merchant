package com.yiyue.store.module.home.store;

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
import com.yiyue.store.databinding.ItemStoreStylistBinding;
import com.yiyue.store.model.vo.bean.StoreManageNexusStyScroolBean;

/**
 *
 * 入驻美发师适配器
 * <p>
 * Created by zm on 2018/10/13.
 */
public class StylistAdapter extends BaseRecycleViewAdapter<StoreManageNexusStyScroolBean>{
    private Context mContext;

    public StylistAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StylistViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_store_stylist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StylistAdapter.StylistViewHolder viewHolder = (StylistAdapter.StylistViewHolder) holder;
        String name = mDatas.get(position).getStylistName();
        String userImg = mDatas.get(position).getCoverImg();
        viewHolder.mBinding.tvName.setText(name);
        ImageView ivImage = viewHolder.mBinding.ivPhoto;
        ImageLoader.loadImage(ivImage,userImg);
    }

    public class StylistViewHolder extends BaseViewHolder {
        private ItemStoreStylistBinding mBinding;
        public StylistViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
