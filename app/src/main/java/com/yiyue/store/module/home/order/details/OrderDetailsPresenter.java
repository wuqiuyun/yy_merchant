package com.yiyue.store.module.home.order.details;

import android.content.Context;

import com.yiyue.store.api.OrderApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.GetOrderResult;

/**
 * Created by zm on 2018/9/27.
 */
public class OrderDetailsPresenter extends BasePresenter<IOrderDetailsView>{

    /**
     * 获取订单详情
     * @param orderId
     */
    public void getOrderDetails(Context context, String orderId) {
        new OrderApi().getOrder(orderId, new YLRxSubscriberHelper<GetOrderResult>(context, true) {
            @Override
            public void _onNext(GetOrderResult baseResponse) {
                getMvpView().onGetOrderDetailsSuccess(baseResponse.getData());
            }
        });
    }
}
