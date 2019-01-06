package com.yiyue.store.module.mine.stylist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemStylistMineBinding;
import com.yiyue.store.model.vo.bean.StylistBean;
import com.yl.core.component.image.ImageLoader;

/**
 * Created by Lizhuo on 2018/10/23.
 */
public class StylistCollectAdapter extends BaseRecycleViewAdapter<StylistBean> {
    private Context context;

    public StylistCollectAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StylistCollectAdapter.StylistViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stylist_mine, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StylistCollectAdapter.StylistViewHolder viewHolder = (StylistCollectAdapter.StylistViewHolder) holder;
        StylistBean item = getDatas().get(position);

        viewHolder.stylistBinding.ratingBar.setOnTouchListener((v, event) -> true);
        if (!TextUtils.isEmpty(item.getStylistCover())) {

            ImageLoader.loadImage(viewHolder.stylistBinding.ivPhoto, item.getStylistCover());
        }
        viewHolder.stylistBinding.tvName.setText(item.getStylistName());

        String mPosition = item.getPosition() != null ? item.getPosition() : item.getProfessor();
        if (!TextUtils.isEmpty(mPosition) && mPosition.length() >= 2) {
            viewHolder.stylistBinding.tvProfessor.setText(mPosition.substring(0, 2));
        } else {
            viewHolder.stylistBinding.tvProfessor.setText("");
        }
        viewHolder.stylistBinding.ratingBar.setRating(item.getGrade());
        viewHolder.stylistBinding.tvCount.setText(String.format(context.getString(R.string.stylist_receipt_count), String.valueOf(item.getReceiptCount())));
        viewHolder.stylistBinding.tvGrade.setText(item.getGrade() + "分");
        viewHolder.stylistBinding.trLocationDistance.setVisibility(View.GONE);

        if (TextUtils.isEmpty(item.getMaxSalesItem())) {
            viewHolder.stylistBinding.tvMaxSalesItem.setVisibility(View.GONE);
        } else {

            viewHolder.stylistBinding.tvMaxSalesItem.setText(item.getMaxSalesItem());
        }
    }

    public class StylistViewHolder extends BaseViewHolder {
        private ItemStylistMineBinding stylistBinding;

        public StylistViewHolder(View itemView) {
            super(itemView);
            stylistBinding = DataBindingUtil.bind(itemView);
        }
    }
}
