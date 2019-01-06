package com.yiyue.store.widget.filter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemSynthesisBinding;
import com.yiyue.store.databinding.ItemSynthesisTwoBinding;
import com.yiyue.store.util.FormatUtil;


/*
 *  @创建者:   27407
 *  @创建时间:  2018/10/15 16:20
 *  @描述：    综合排序选择器
 */

public class SynthesisAdapter extends BaseRecycleViewAdapter<String> {

    private Context mContext;
    private int mType;
    private int tempPosition;
    public SynthesisAdapter(Context context, int type) {
        mContext = context;
        mType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mType==0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_synthesis, parent, false);
           return new SynthesisViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_synthesis_two, parent, false);
            return new SynthesisViewTwoHolder(view);
        }
    }

    public void setTempPosition(int tempPosition) {
        this.tempPosition = tempPosition;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mType==0) {
            SynthesisViewHolder viewHolder = (SynthesisViewHolder) holder;
            viewHolder.mBinding.tvType.setText(FormatUtil.Formatstring(mDatas.get(position)));
            if (tempPosition==position){
                viewHolder.mBinding.tvType.setTextColor(mContext.getResources().getColor(R.color.color_FFA200));
            }
        }else {
                SynthesisViewTwoHolder viewHolder = (SynthesisViewTwoHolder) holder;
                if (tempPosition==position){
                    viewHolder.itemView.setBackgroundColor(Color.WHITE);
                }
                viewHolder.mBinding.tvType.setText(FormatUtil.Formatstring(mDatas.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class SynthesisViewHolder extends BaseViewHolder {
        private ItemSynthesisBinding mBinding;

        public SynthesisViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
    public class SynthesisViewTwoHolder extends BaseViewHolder {
        private ItemSynthesisTwoBinding mBinding;

        public SynthesisViewTwoHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
