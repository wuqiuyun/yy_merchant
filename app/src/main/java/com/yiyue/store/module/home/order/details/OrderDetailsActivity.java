package com.yiyue.store.module.home.order.details;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityOrderDetailsBinding;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.OrderDetailsBean;
import com.yiyue.store.model.vo.bean.OrderRefundBean;
import com.yiyue.store.util.BigDecimalUtils;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

/**
 * 订单详情
 * <p>
 * Create by zm on 2018/10/27
 */
@CreatePresenter(OrderDetailsPresenter.class)
public class OrderDetailsActivity extends BaseMvpAppCompatActivity<IOrderDetailsView, OrderDetailsPresenter>
        implements IOrderDetailsView{
    private static final String EXTRAS_ORDER_ID = "order_id";

    private ActivityOrderDetailsBinding detailsBinding;
    private String orderId;
    /**
     * start orderId
     * @param context
     * @param orderId 订单id
     */
    public static void startActivity (Context context,@NonNull String orderId) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_ORDER_ID, orderId);
        startActivity(context, OrderDetailsActivity.class, bundle);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        hasExtras();
        initView();
        loadData();
    }

    private void hasExtras () {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString(EXTRAS_ORDER_ID);
        }
        // orderId不能为空
        if (TextUtils.isEmpty(orderId)) {
            finish();
            return;
        }
    }

    private void initView () {
        StatusBarUtil.setLightMode(this);
        detailsBinding = (ActivityOrderDetailsBinding) viewDataBinding;
        // 返回
        detailsBinding.titleView.setLeftClickListener(v -> {
            finish();
        });
    }

    private void loadData() {
        getMvpPresenter().getOrderDetails(this, orderId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.NewMessage message) {
        loadData();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onGetOrderDetailsSuccess(OrderDetailsBean data) {
        // 头像
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setErrorResId(R.drawable.icon_head_pic_def)
                .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                .setAsBitmap(true)
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .setPrioriy(ImageLoaderConfig.LoadPriority.HIGH)
                .build();
        ImageLoader.loadImage(detailsBinding.ivPhoto, data.getUserPath(), config, null);

        // 标签
        detailsBinding.tvLabel.setText(FormatUtil.Formatstring(data.getUserLabel()));
        // 姓名
        detailsBinding.tvName.setText(FormatUtil.Formatstring(data.getUserNickname()));
        // 性别
        setDrawableRight(detailsBinding.tvName, ContextCompat.getDrawable(this,
                data.getUserGender() == 1 ? R.drawable.icon_boy : R.drawable.icon_girl));
        // 服务项目
        detailsBinding.orderProject.setContentText(data.getOrdername());
        // 服务门店
        String name = data.getStylistname();
        if (name.length() > 8){

           String updateName = name.substring(0 , 8) + "..." ;
            detailsBinding.orderStore.setContentText(updateName);

        }else {

            detailsBinding.orderStore.setContentText(name);
        }
        detailsBinding.orderStore.setSubContentText(data.getNexus() == 0 ? "(平台)" : "(店内)");
        // 预约时间
        detailsBinding.orderDateRese.setContentText(DateUtil.date2Str(data.getOrdertime(), DateUtil.FORMAT_YMDHM));
        detailsBinding.orderDateRese.setSubContentText(data.getDatename());
        // 订单编号
        detailsBinding.orderId.setContentText(data.getOrderno());
        // 原预约时间
        if (data.getOldordertime() > 0L) {
            detailsBinding.orderDatePromise.setVisibility(View.VISIBLE);
            detailsBinding.orderDatePromise.setContentText(DateUtil.date2Str(data.getOldordertime(), DateUtil.FORMAT_YMDHM));
        }
        // 开始时间
        if (data.getStarttime() > 0L) {
            detailsBinding.orderDateStart.setVisibility(View.VISIBLE);
            detailsBinding.orderDateStart.setContentText(DateUtil.date2Str(data.getStarttime(), DateUtil.FORMAT_YMDHM));
        }
        // 完成时间
        if (data.getEndtime() > 0L) {
            detailsBinding.orderDateStop.setVisibility(View.VISIBLE);
            detailsBinding.orderDateStop.setContentText(DateUtil.date2Str(data.getEndtime(), DateUtil.FORMAT_YMDHM));
        }
        // 取消时间
        if (data.getCanceltime() > 0L) {
            detailsBinding.orderDateStart.setVisibility(View.VISIBLE);
            detailsBinding.orderDateStart.setTitleText("取消时间：");
            detailsBinding.orderDateStart.setContentText(DateUtil.date2Str(data.getCanceltime(), DateUtil.FORMAT_YMDHM));
        }
        // 退款时间
        if (data.getRefundtime() > 0L) {
            detailsBinding.orderDateStop.setVisibility(View.VISIBLE);
            detailsBinding.orderDateStop.setTitleText("退款时间：");
            detailsBinding.orderDateStop.setContentText(DateUtil.date2Str(data.getRefundtime(), DateUtil.FORMAT_YMDHM));
        }
        // 订单金额
        detailsBinding.orderAmount.setContentText(String.format(getString(R.string.RMB), String.valueOf(data.getOrderamount())));
        // 支付金额
        detailsBinding.payAmount.setContentText(String.format(getString(R.string.RMB), String.valueOf(data.getPayamount())));
        // 订单备注
        detailsBinding.orderRemarks.setContentText(data.getRemark());
        // 预计收入
        if (data.getStatus() == 3 ||data.getStatus() == 4 || data.getStatus() == 6) {
            detailsBinding.clearAmount.setContentText(String.format(getString(R.string.RMB), String.valueOf(data.getClearamount())));
        }else {
            detailsBinding.clearAmount.setVisibility(View.GONE);
        }
        // 优惠金额
        if (data.getServicemodel() == 2) {
            detailsBinding.couponAmount.setTitleText("支付方式：");
            detailsBinding.couponAmount.setContentText("套餐券");
            detailsBinding.payAmount.setVisibility(View.GONE);
        }else {
            detailsBinding.couponAmount.setTitleText("优惠金额：");
            detailsBinding.couponAmount.setSubContentText(data.getCoupondirection() ==1 ? "(平台优惠券)" : "(美发师优惠券)");
            detailsBinding.couponAmount.setContentText(String.format(getString(R.string.RMB),"0"));
            switch (data.getCoupontype()) {
                case 1:
                case 3:
                    detailsBinding.couponAmount.setContentText(String.format(getString(R.string.RMB), String.valueOf(data.getCouponamount())));
                    break;
                case 2:
                    detailsBinding.couponAmount.setContentText(String.format(getString(R.string.RMB),
                            String.valueOf(BigDecimalUtils.formatRoundUp(data.getOrderamount() * (1 - data.getCouponamount() / 10), BigDecimalUtils.MONEY_POINT))));
                    break;
            }
        }
        // 退款
        OrderRefundBean orderRefundBean = data.getOrderRefund();
        if (orderRefundBean != null) {
            detailsBinding.refundAmount.setVisibility(View.VISIBLE);
            detailsBinding.refundAmount.setContentText(String.format(getString(R.string.RMB), String.valueOf(orderRefundBean.getRefundamount())));
            detailsBinding.orderHandlingFee.setVisibility(View.VISIBLE);
            detailsBinding.orderHandlingFee.setContentText(String.format(getString(R.string.RMB), String.valueOf(orderRefundBean.getHandlingfee())));
            detailsBinding.orderHandlingFee.setSubContentText("（项目价格*5%）");
        }
    }

    private void setDrawableRight(TextView textView, Drawable drawableRight) {
        drawableRight.setBounds(0, 0,drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawableRight, null);
    }
}
