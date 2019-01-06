package com.yiyue.store.module.mine.stylist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yl.core.component.image.ImageLoader;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemStylistBinding;
import com.yiyue.store.model.vo.bean.StylistBean;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.image.ImageLoaderConfig;

/**
 * Created by zm on 2018/10/10.
 */
public class StylistAdapter extends BaseRecycleViewAdapter<StylistBean>{
    private Context context;

    public StylistAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StylistViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stylist, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StylistViewHolder viewHolder = (StylistViewHolder) holder;
        StylistBean stylistBean = mDatas.get(position);
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setAsBitmap(true)
                .setCornerRadius(5)
                .setRoundedCorners(true)
                .setPlaceHolderResId(com.yl.core.R.drawable.bg_image_placeholder)
                .setErrorResId(com.yl.core.R.drawable.bg_image_placeholder)
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .setPrioriy(ImageLoaderConfig.LoadPriority.NORMAL)
                .build();
        ImageLoader.loadImage(viewHolder.stylistBinding.ivPhoto, stylistBean.getHeadPortrait(), config, null);
        viewHolder.stylistBinding.ratingBar.setRating(stylistBean.getStar());
        viewHolder.stylistBinding.ratingBar.setOnTouchListener((v, event) -> true);
        viewHolder.stylistBinding.tvName.setText(FormatUtil.Formatstring(stylistBean.getNickname()));
        viewHolder.stylistBinding.tvTotalPerformance.setText(FormatUtil.Formatstring(stylistBean.getWaitVerification()+"单"));
        viewHolder.stylistBinding.tvAmount.setText(FormatUtil.Formatstring("￥"+ stylistBean.getTotalPerformance()));
    }

    public class StylistViewHolder extends BaseViewHolder {
        private ItemStylistBinding stylistBinding;

        public StylistViewHolder(View itemView) {
            super(itemView);
            stylistBinding = DataBindingUtil.bind(itemView);
        }
    }
}
