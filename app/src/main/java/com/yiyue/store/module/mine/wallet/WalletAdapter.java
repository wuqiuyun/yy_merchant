package com.yiyue.store.module.mine.wallet;

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
import com.yiyue.store.databinding.ItemWalletProjectBinding;
import com.yiyue.store.model.vo.bean.CashInfoAccountBean;

/**
 * Created by zm on 2018/10/8.
 */
public class WalletAdapter extends BaseRecycleViewAdapter<CashInfoAccountBean>{
    private Context context;

    public WalletAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletViewHolder(LayoutInflater.from(context).inflate(R.layout.item_wallet_project, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WalletAdapter.WalletViewHolder viewHolder = (WalletAdapter.WalletViewHolder) holder;
        viewHolder.mBinding.tvDate.setText(mDatas.get(position).getCreatetime());
        String remark = mDatas.get(position).getRemark();
        if (!TextUtils.isEmpty(remark)&&remark!=null){
            viewHolder.mBinding.tvHint.setText(remark);
            viewHolder.mBinding.tvHint.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mBinding.tvHint.setVisibility(View.INVISIBLE);
        }
        int status = mDatas.get(position).getStatus();//1增加 0减少'
        if (status==0){
            viewHolder.mBinding.tvProjectType.setText("支出项目");
            viewHolder.mBinding.tvPrice.setText("-"+mDatas.get(position).getChangebalance());
            viewHolder.mBinding.tvPrice.setTextColor(context.getResources().getColor(R.color.color_FFA200));
        }else {
            viewHolder.mBinding.tvProjectType.setText("收入项目");
            viewHolder.mBinding.tvPrice.setText("+"+mDatas.get(position).getChangebalance());
            viewHolder.mBinding.tvPrice.setTextColor(context.getResources().getColor(R.color.color_red));
        }

    }

    private class WalletViewHolder extends BaseViewHolder {
        private ItemWalletProjectBinding mBinding;
        public WalletViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
