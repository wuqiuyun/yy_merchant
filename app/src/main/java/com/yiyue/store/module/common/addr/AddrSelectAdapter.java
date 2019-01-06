package com.yiyue.store.module.common.addr;

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
import com.yiyue.store.databinding.ItemAddrSelectBinding;
import com.yiyue.store.model.vo.bean.AddrInfoBean;

/**
 * Created by zm on 2018/10/19.
 */
public class AddrSelectAdapter extends BaseRecycleViewAdapter<AddrInfoBean>{
    private Context mContext;

    public AddrSelectAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddrSelectViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_addr_select, parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AddrInfoBean infoBean = mDatas.get(position);
        AddrSelectViewHolder viewHolder = (AddrSelectViewHolder) holder;
        // 地址
        if (TextUtils.isEmpty(infoBean.getAddr())) {
            viewHolder.mBinding.tvAddr.setVisibility(View.GONE);
        }else {
            viewHolder.mBinding.tvAddr.setText(infoBean.getAddr());
        }
        // 详细地址
        if (TextUtils.isEmpty(infoBean.getAddrDetail())) {
            viewHolder.mBinding.tvAddrDetails.setVisibility(View.GONE);
        }else {
            viewHolder.mBinding.tvAddrDetails.setText(infoBean.getAddrDetail());
        }
    }

    private class AddrSelectViewHolder extends BaseViewHolder {
        private ItemAddrSelectBinding mBinding;

        public AddrSelectViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
