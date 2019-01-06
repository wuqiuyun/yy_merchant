package com.yiyue.store.module.mine.wallet.bill;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemIncomeBillBinding;
import com.yiyue.store.model.vo.bean.TotalBillBean;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;

/**
 * 总收入/总支出账单
 * Created by wqy on 2018/12/18.
 */

public class BillAdapter extends BaseRecycleViewAdapter<TotalBillBean> {
    private Context context;
    private int billType = 1; //账单类型 1:收入 2：支出
    private ImageLoaderConfig config;

    public BillAdapter(Context context) {
        this.context = context;
    }

    public void getType(int type) {
        this.billType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomeBillViewHolder(LayoutInflater.from(context).inflate(R.layout.item_income_bill, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TotalBillBean bean = getDatas().get(position);
        IncomeBillViewHolder viewHolder = (IncomeBillViewHolder) holder;
        viewHolder.mBinding.tvName.setText(bean.getInName());
        viewHolder.mBinding.tvRemark.setText("[" + bean.getRemark() + "]");
        viewHolder.mBinding.tvCreateTime.setText(bean.getCreateTime());

        if (config == null) {
            config = new ImageLoaderConfig.Builder().
                    setAsBitmap(true).
                    setPlaceHolderResId(R.drawable.icon_servicerevenue).
                    setErrorResId(R.drawable.icon_servicerevenue).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        }
        int inType = bean.getInType();
        if (billType == 1) {//收入
            viewHolder.mBinding.tvAmount.setText("+" + bean.getAmount());
            if (inType == 1) { //注册奖金
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_bonus);
            } else if (inType == 2) {//推广佣金
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_commission);
            } else if (inType == 3) {//红包
                ImageLoader.loadImage(viewHolder.mBinding.ivPic, bean.getUserImg(), config, null);
            } else if (inType == 4) {//服务收入
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_servicerevenue);
            } else if (inType == 5) {//转账
                ImageLoader.loadImage(viewHolder.mBinding.ivPic, bean.getUserImg(), config, null);
            } else if (inType == 6) {//订单退款
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_servicerevenue);
            } else if (inType == 7) {//提现驳回
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_propose_nor);
            } else if (inType == 8) {//系统清算
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_system_nor);
            } else if (inType == 9) {//转可提现
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_propose_nor);
            } else {//其他
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_other_nor);
            }
        } else {//支出
            viewHolder.mBinding.tvAmount.setText("-" + bean.getAmount());
            if (inType == 1) {//提现
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_propose_nor);
            } else if (inType == 2) {//转账
                ImageLoader.loadImage(viewHolder.mBinding.ivPic, bean.getUserImg());
            } else if (inType == 3) {//红包
                ImageLoader.loadImage(viewHolder.mBinding.ivPic, bean.getUserImg());
            } else if (inType == 4) {//违约扣款
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_deduction_nor);
            } else if (inType == 5) {//购买
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_servicerevenue);
            } else if (inType == 6) {//系统清算
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_system_nor);
            } else {//其他
                viewHolder.mBinding.ivPic.setImageResource(R.drawable.icon_other_nor);
            }
        }
    }

    private class IncomeBillViewHolder extends BaseViewHolder {
        ItemIncomeBillBinding mBinding;

        public IncomeBillViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
