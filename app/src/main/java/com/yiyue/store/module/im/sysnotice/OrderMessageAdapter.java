package com.yiyue.store.module.im.sysnotice;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemOrderMessageBinding;
import com.yiyue.store.model.vo.bean.daobean.OrderMessageBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lizhuo on 2018/11/13.
 * 系统消息——订单交易 适配器
 */
public class OrderMessageAdapter extends BaseRecycleViewAdapter<OrderMessageBean> {
    private Context context;

    public OrderMessageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderMessageViewHolder viewHolder = (OrderMessageViewHolder) holder;
        OrderMessageBean orderMessage = getDatas().get(position);

        if (orderMessage.getMsgtype()==16){
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String oldTime = dateFormat1.format(new Date(orderMessage.getOldordertime()));
            viewHolder.mBinding.tvServiceOldtime.setVisibility(View.VISIBLE);
            viewHolder.mBinding.tvServiceOldtime.setText("原预约时间：" +oldTime);
        }else {
            viewHolder.mBinding.tvServiceOldtime.setVisibility(View.GONE);
        }

        viewHolder.mBinding.tvTitle.setText(orderMessage.getTitle());
        viewHolder.mBinding.tvContent.setText(orderMessage.getContent());
        viewHolder.mBinding.tvServiceName.setText("服务：" + orderMessage.getServerName());

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String serviceTime = dateFormat1.format(new Date(orderMessage.getOrdertime()));

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd HH:mm");
        String caretaTime = dateFormat2.format(new Date(orderMessage.getCreatetime()));

        if (orderMessage.getMsgtype() != 13){
            viewHolder.mBinding.tvServiceTime.setText("预约时间：" + serviceTime);
        } else {
            viewHolder.mBinding.tvServiceTime.setText("");//套餐券购买 没有预约时间
        }
        viewHolder.mBinding.tvTime.setText(caretaTime);
        viewHolder.mBinding.tvOrderNo.setText("订单编号：" + orderMessage.getOrderNo());
    }

    public class OrderMessageViewHolder extends BaseViewHolder {
        private ItemOrderMessageBinding mBinding;

        public OrderMessageViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
