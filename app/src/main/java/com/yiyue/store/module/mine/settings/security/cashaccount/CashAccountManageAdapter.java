package com.yiyue.store.module.mine.settings.security.cashaccount;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.util.BankImgUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.widget.MyRoundLayout;

import java.util.List;

/**
 * Created by lyj on 2018/11/8.
 */
public class CashAccountManageAdapter extends BaseQuickAdapter<CashAliBean, BaseViewHolder> {


    public CashAccountManageAdapter(List<CashAliBean> data) {
        super(R.layout.item_cashaccount_manage, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CashAliBean bean) {
        if (bean != null) {
            baseViewHolder.addOnClickListener(R.id.btn_delete).addOnClickListener(R.id.rl_bankcard);
            //银行名称
            baseViewHolder.setText(R.id.tv_bank_name, FormatUtil.Formatstring(bean.getTypeName()));
            //银行卡号
            baseViewHolder.setText(R.id.tv_bank_cardid,  StringUtil.getBankCardumber(FormatUtil.Formatstring(bean.getAccountno())));
            String imgNa = bean.getShortName();
            //ICBC 工商银行 CBC 建设银行 ABC 农业银行 BOC 中国银行 BCM 交通银行 CMB 招商银行  CIB 兴业银行 PSBC 邮政储蓄银行 CNCB 中信银行 CMBC 民生银行 PAB 平安银行 CEB 光大银行
            baseViewHolder.setImageResource(R.id.iv_bank_name, BankImgUtil.getBankImgTwo(imgNa));
            baseViewHolder.setBackgroundRes(R.id.rl_bankcard, BankImgUtil.getBankImgBag(imgNa));
        }
    }

}
