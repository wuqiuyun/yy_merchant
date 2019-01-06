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
import com.yiyue.store.util.FormatKmUtil;
import com.yl.core.component.image.ImageLoader;

/**
 * Created by Lizhuo on 2018/10/23.
 */
public class StylistMineAdapter extends BaseRecycleViewAdapter<StylistBean> {
    private Context context;

    public StylistMineAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StylistMineAdapter.StylistViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stylist_mine, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StylistMineAdapter.StylistViewHolder viewHolder = (StylistMineAdapter.StylistViewHolder) holder;
        StylistBean item = getDatas().get(position);

        viewHolder.stylistBinding.ratingBar.setOnTouchListener((v, event) -> true);
        ImageLoader.loadImage(viewHolder.stylistBinding.ivPhoto, item.getHeadPortrait());
        viewHolder.stylistBinding.tvName.setText(item.getNickname());
        viewHolder.stylistBinding.tvGrade.setText(item.getStar() != 0.0 ? item.getStar() + "分" : item.getGrade() + "分");
        viewHolder.stylistBinding.tvLocationDistance.setText(FormatKmUtil.FormatKmStr(item.getDistance()));

        if (!TextUtils.isEmpty(item.getPosition())) {
            viewHolder.stylistBinding.tvProfessor.setText(item.getPosition().substring(0, 2));
        }
        viewHolder.stylistBinding.ratingBar.setRating(item.getStar() != 0.0 ? item.getStar() : item.getGrade());
        viewHolder.stylistBinding.tvCount.setText(String.format(context.getString(R.string.stylist_receipt_count),
                item.getMonthOrder() != 0 ? String.valueOf(item.getMonthOrder()) : String.valueOf(item.getReceiptCount())));

        if (TextUtils.isEmpty(item.getService()) || item.getPrice() == 0) {
            viewHolder.stylistBinding.tvMaxSalesItem.setVisibility(View.GONE);
        } else {

            viewHolder.stylistBinding.tvMaxSalesItem.setText(item.getService() + item.getPrice() + "元");
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
