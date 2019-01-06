package com.yiyue.store.module.mine.wallet.bill.card;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemCardDetailsBinding;
import com.yiyue.store.model.vo.bean.BankDetailsBean;
import com.yiyue.store.util.BankImgUtil;


/**
 * Created by wqy on 2018/12/18.
 */

public class CardDetailsAdapter extends BaseRecycleViewAdapter<BankDetailsBean.TakeLogsBean> {
    private Context context;
    private String shortName = "";

    public CardDetailsAdapter(Context context) {
        this.context = context;
    }

    public void getBankName(String shortName) {
        this.shortName = shortName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomeBillViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card_details, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        IncomeBillViewHolder viewHolder = (IncomeBillViewHolder) holder;
        BankDetailsBean.TakeLogsBean bean = getDatas().get(position);
        viewHolder.mBinding.tvAmount.setText("-" + bean.getAmount());
        viewHolder.mBinding.tvRemark.setText(bean.getRemark());
        String creatTime = bean.getCreateTime();
        if (!TextUtils.isEmpty(creatTime)) {
            String m = creatTime.substring(5, 7);
            String d = creatTime.substring(8, 10);
            String time = m + "月" + d + "日";
            viewHolder.mBinding.tvTime.setText(time);
        }

        //银行 logo
        setLogo(viewHolder.mBinding.ivLogo);
    }

    private class IncomeBillViewHolder extends BaseViewHolder {
        ItemCardDetailsBinding mBinding;

        public IncomeBillViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    //银行logo
    private void setLogo(ImageView imageView) {
        imageView.setImageDrawable(context.getResources().getDrawable(BankImgUtil.getBankImg(shortName)));
    }

}
