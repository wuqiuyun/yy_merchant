package com.yiyue.store.module.mine.wallet.statistics.StylistStatistics;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.model.vo.bean.StoreSuccessOrdersBean;
import com.yiyue.store.util.TypeConvertUtils;
import com.yl.core.component.image.ImageLoader;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemOrderStatisticsBinding;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.image.ImageLoaderConfig;

import java.text.DecimalFormat;


/*
 *  @项目名：  my_merchant 
 *  @包名：    com.yiyue.store.module.home.orders
 *  @文件名:   StylistStatisticsAdapter
 *  @创建者:   27407
 *  @创建时间:  2018/10/14 14:06
 *  @描述：    订单统计适配器
 */

public class StylistStatisticsAdapter extends BaseRecycleViewAdapter<StoreSuccessOrdersBean> {

    private Context mContext;
    private final DecimalFormat mDecimalFormat;

    public StylistStatisticsAdapter(Context context) {
        mContext = context;
        mDecimalFormat = new DecimalFormat("0.00");

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_statistics, parent, false);
        return new OrderStatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderStatisticsViewHolder viewHolder = (OrderStatisticsViewHolder) holder;
        StoreSuccessOrdersBean bean = mDatas.get(position);

        viewHolder.mBinding.tvUserName.setText(bean.getName());
        viewHolder.mBinding.tvCompleteOrder.setText(String.format(mContext.getString(R.string.order_number), FormatUtil.Formatstring(String.valueOf(bean.getNum()))));
        if (bean.getMoney() != null) {
            viewHolder.mBinding.tvMoney.setText(String.format(mContext.getString(R.string.RMB), FormatUtil.Formatstring(mDecimalFormat.format(TypeConvertUtils.convertToDouble(bean.getMoney(), 0)))));
        }

        if (bean.getNexus() != null&&!bean.getNexus().equals("null")) {
            if (bean.getNexus().equals("0")){
                viewHolder.mBinding.tvNexus.setText(mContext.getString(R.string.stylist_join));
            }else{
                viewHolder.mBinding.tvNexus.setText(mContext.getString(R.string.stylist_signing));
            }
        }
        // 头像
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setAsBitmap(true)
                .setCropType(ImageLoaderConfig.CENTER_INSIDE)
                .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                .setErrorResId(R.drawable.icon_head_pic_def)
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .setPrioriy(ImageLoaderConfig.LoadPriority.NORMAL)
                .build();
        ImageLoader.loadImage(viewHolder.mBinding.civHeadPhoto, bean.getHeadImg(), config, null);
    }

    public class OrderStatisticsViewHolder extends BaseViewHolder {
        private ItemOrderStatisticsBinding mBinding;

        public OrderStatisticsViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
