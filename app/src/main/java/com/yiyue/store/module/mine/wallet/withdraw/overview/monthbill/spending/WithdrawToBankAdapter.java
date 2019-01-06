package com.yiyue.store.module.mine.wallet.withdraw.overview.monthbill.spending;

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
import com.yiyue.store.databinding.ItemWithdrawToBankBinding;
import com.yiyue.store.model.vo.bean.BankSumBean;
import com.yiyue.store.util.BankImgUtil;


/**
 * 提现到银行Adapter
 */
public class WithdrawToBankAdapter extends BaseRecycleViewAdapter<BankSumBean> {
    private Context context;

    public WithdrawToBankAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WithdrawToBankHolder(LayoutInflater.from(context).inflate(R.layout.item_withdraw_to_bank, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WithdrawToBankHolder bankHolder = (WithdrawToBankHolder) holder;
        BankSumBean bean = getDatas().get(position);
        bankHolder.mBinding.tvMoney.setText(String.valueOf(bean.getSumMoney()));
        int length = bean.getAccountno().length();
        if (length >= 4) {
            bankHolder.mBinding.tvTitle.setText(bean.getBankName() + "(" + bean.getAccountno().substring(length - 4, length) + ")");
        }
        // 设置银行logo
        setLogo(bankHolder.mBinding.ivIcon, bean.getShortName());
    }

    private class WithdrawToBankHolder extends BaseViewHolder {
        private ItemWithdrawToBankBinding mBinding;

        public WithdrawToBankHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    //银行logo
    private void setLogo(ImageView imageView, String shortName) {
        imageView.setImageDrawable(context.getResources().getDrawable(BankImgUtil.getBankImg(shortName)));
    }

}
