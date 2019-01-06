package com.yiyue.store.api;

import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.GetOrderListResult;
import com.yiyue.store.model.vo.result.GetOrderResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by zm on 2018/10/27.
 */
public class OrderApi {

    private interface Api {
        // 门店订单查询
        @GET("/store-api/order/getOrderPage")
        Observable<GetOrderListResult> getOrderPage(@QueryMap Map<String, Object> params);

        // 订单详情查询
        @GET("/store-api/order/getOrder")
        Observable<GetOrderResult> getOrder(@Query("orderId") String orderId);
    }

    private Api mApi;

    public OrderApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 获取订详情
     * @param orderId
     */
    public void getOrder(String orderId, YLRxSubscriberHelper<GetOrderResult> subscriberHelper) {
        mApi.getOrder(orderId)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     *
     * @param status
     * @param page
     * @param size
     * @param subscriberHelper
     */
    public void getOrderPage(int status, int page, int size, YLRxSubscriberHelper<GetOrderListResult> subscriberHelper) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("page", page);
        params.put("size", size);
        params.put("storeId", AccountManager.getInstance().getStoreId());
        mApi.getOrderPage(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
}
