package com.yiyue.store.module.mine.wallet.withdraw.account;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yiyue.store.databinding.ItemSelectAccountwithdrawBinding;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.util.BankImgUtil;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.log.DLog;

/**
 * 选择提现账户适配器
 * Created by lyj on 2018/11/12.
 */

class AccountWithdrawSelectionAdapter extends BaseRecycleViewAdapter<CashAliBean> {
    private LayoutInflater inflater;
    private Context contexts;

    public AccountWithdrawSelectionAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.contexts = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountWithdrawSelectionViewHolder(inflater.inflate(R.layout.item_select_accountwithdraw, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AccountWithdrawSelectionViewHolder viewHolder = (AccountWithdrawSelectionViewHolder) holder;
        CashAliBean bean = getDatas().get(position);
        viewHolder.selectStoreBinding.cbStatus.setChecked(bean.isStatus());
        String subStr = bean.getAccountno().substring( bean.getAccountno().length()-4, bean.getAccountno().length());
        //银行名称
        viewHolder.selectStoreBinding.tvAccountName.setText(FormatUtil.Formatstring(bean.getTypeName())+"("+subStr+")");
        //银行卡详情
        viewHolder.selectStoreBinding.tvAccountDetail.setText("尾号"+subStr+"储蓄卡");
        String imgNa = bean.getShortName();
        DLog.e("onBindViewHolder----"+imgNa);
        //ICBC 工商银行 CBC 建设银行 ABC 农业银行 BOC 中国银行 BCM 交通银行 CMB 招商银行  CIB 兴业银行 PSBC 邮政储蓄银行 CNCB 中信银行 CMBC 民生银行 PAB 平安银行 CEB 光大银行
        if (bean.getShortName().equals("ALI")) {
            viewHolder.selectStoreBinding.ivAccountImg.setImageResource(R.drawable.icon_alipay);
            return;
        }
        viewHolder.selectStoreBinding.ivAccountImg.setImageResource(BankImgUtil.getBankImg(imgNa));
    }

    private class AccountWithdrawSelectionViewHolder extends BaseViewHolder {
        ItemSelectAccountwithdrawBinding selectStoreBinding;

        public AccountWithdrawSelectionViewHolder(View itemView) {
            super(itemView);
            selectStoreBinding = DataBindingUtil.bind(itemView);
        }
    }
}
