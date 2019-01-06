package com.yiyue.store.module.home.order;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemOrderBinding;
import com.yiyue.store.model.constant.OrderStatus;
import com.yiyue.store.model.vo.bean.OrderBean;
import com.yiyue.store.util.ColorUtil;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.util.DateUtil;

import org.greenrobot.greendao.annotation.NotNull;

import static com.yiyue.store.model.constant.OrderStatus.ORDER_COMPLETE;
import static com.yiyue.store.model.constant.OrderStatus.ORDER_CONFIRM;
import static com.yiyue.store.model.constant.OrderStatus.ORDER_REFUND;
import static com.yiyue.store.model.constant.OrderStatus.ORDER_SERVICE;

/**
 * Created by zm on 2018/9/20.
 */
public class OrderAdapter extends BaseRecycleViewAdapter<OrderBean>{
    private Context context;
    @OrderStatus.OrderType
    private int orderType; // 订单类型

    public OrderAdapter(Context context,    @OrderStatus.OrderType int orderType) {
        this.context = context;
        this.orderType = orderType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemOrderBinding orderBinding = ((OrderViewHolder) holder).mOrderBinding;

        OrderBean orderBean = mDatas.get(position);
        // 头像
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setErrorResId(R.drawable.icon_head_pic_def)
                .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                .setAsBitmap(true)
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .setPrioriy(ImageLoaderConfig.LoadPriority.HIGH)
                .build();
        ImageLoader.loadImage(orderBinding.ivPhoto, orderBean.getUserPath(), config, null);
        // 订单编号
        orderBinding.orderId.setContentText(orderBean.getOrderno());
        // 用户昵称

        String userName = FormatUtil.Formatstring(orderBean.getUserNickname());

        if (userName.length() > 8){

            String upUserName = userName.substring(0 , 8) + "..." ;
            orderBinding.tvName.setText(upUserName);
        }else {

            orderBinding.tvName.setText(userName);
        }
        // 订单类型
        orderBinding.tvOrderType.setText("");
        // 美发师名称
        String stylistName = FormatUtil.Formatstring(orderBean.getStylistname());

        if (stylistName.length() > 8){

            String upStylistName = stylistName.substring( 0 , 8) + "..." ;

            orderBinding.orderStore.setContentText(upStylistName);

        }else {
            orderBinding.orderStore.setContentText(stylistName);

        }

        orderBinding.orderStore.setSubContentText(orderBean.getNexus() == 0 ? "(平台)" : "(店内)");
        // 服务项目
        orderBinding.orderProject.setContentText(orderBean.getOrdername());
        // 优惠券
        switch (orderBean.getCoupontype()) {
            case 0:
                orderBinding.orderCoupon.setVisibility(View.GONE);
                break;
            case 1:
                orderBinding.orderCoupon.setVisibility(View.VISIBLE);
                orderBinding.orderCoupon.setContentText("满减券");
                break;
            case 2:
                orderBinding.orderCoupon.setVisibility(View.VISIBLE);
                orderBinding.orderCoupon.setContentText("折扣券");
                break;
            case 3:
                orderBinding.orderCoupon.setVisibility(View.VISIBLE);
                orderBinding.orderCoupon.setContentText("抵扣券");
                break;
        }
        // 性别
        setDrawableRight(orderBinding.tvName,
                ContextCompat.getDrawable(context,
                        orderBean.getUserGender() == 1 ? R.drawable.icon_boy : R.drawable.icon_girl));
        // 服务时间
        orderBinding.orderDate.setSubContentText(orderBean.getDatename());

        // 订单金额
        if (orderBean.getServicemodel() == 2){
            orderBinding.orderProject.setRightText(String.format(context.getString(R.string.RMB), orderBean.getOrderamount()+""));
        }else{
            orderBinding.orderProject.setRightText(String.format(context.getString(R.string.RMB), orderBean.getPayamount() + ""));
        }

        switch (orderType) {
            case ORDER_CONFIRM:
                // 服务时间
                orderBinding.orderDate.setTitleText(context.getString(R.string.order_date));
                orderBinding.orderDate.setContentText(DateUtil.date2Str(orderBean.getOrdertime(), DateUtil.FORMAT_YMDHM));

                orderBinding.tvOrderType.setTextColor(ColorUtil.getColor(R.color.color_28C8B5));
                break;
            case ORDER_SERVICE:
                // 服务时间
                orderBinding.orderDate.setTitleText(context.getString(R.string.order_date));
                orderBinding.orderDate.setContentText(DateUtil.date2Str(orderBean.getOrdertime(), DateUtil.FORMAT_YMDHM));

                orderBinding.tvOrderType.setText("");
                break;
            case ORDER_COMPLETE:
                // 服务时间
                orderBinding.orderDate.setTitleText(context.getString(R.string.order_date_stop));
                orderBinding.orderDate.setContentText(DateUtil.date2Str(orderBean.getEndtime(), DateUtil.FORMAT_YMDHM));

                orderBinding.tvOrderType.setText("");
                break;
            case ORDER_REFUND:
                // 服务时间
                orderBinding.orderDate.setTitleText(context.getString(R.string.order_date_stop));
                orderBinding.orderDate.setContentText(DateUtil.date2Str(orderBean.getRefundtime(), DateUtil.FORMAT_YMDHM));

                orderBinding.tvOrderType.setTextColor(ColorUtil.getColor(R.color.color_CCCCCC));
                // 查看详情
                orderBinding.llOrderDetails.setVisibility(View.GONE);
                break;
        }

        // 订单状态
        switch (orderBean.getStatus()) {
            // ORDER_CONFIRM
            case 8:
                orderBinding.tvOrderType.setText("加价待通过");
                break;
            case 14:
                orderBinding.tvOrderType.setText("用户取消订单");
                break;
            // ORDER_SERVICE
            case 4:
                break;
            case 6:
                break;
            case 7:
                break;
            case 9:
                break;
            case 10:
                break;
            // ORDER_COMPLETE
            case 11:
                break;
            case 18:
                break;
            // ORDER_REFUND
            case 12:
                orderBinding.tvOrderType.setText("美发师已取消");
                break;
            case 13:
                orderBinding.tvOrderType.setText("用户已取消");
                break;
            case 15:
                orderBinding.tvOrderType.setText("用户已取消");
                break;
        }
    }

    private void setDrawableRight(TextView textView, @NotNull Drawable drawableRight) {
        drawableRight.setBounds(0, 0,drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawableRight, null);
    }

    public class OrderViewHolder extends BaseViewHolder {
        private ItemOrderBinding mOrderBinding;

        public OrderViewHolder(View itemView) {
            super(itemView);
            mOrderBinding = DataBindingUtil.bind(itemView);
        }
    }

}
