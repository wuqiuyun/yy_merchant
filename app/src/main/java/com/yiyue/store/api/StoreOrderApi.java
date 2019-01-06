package com.yiyue.store.api;

/*
    订单管理
 * Create by lvlong on  2018/10/26
 */

import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.AllOrderDetailResult;
import com.yiyue.store.model.vo.result.OrderChartResult;
import com.yiyue.store.model.vo.result.StoreSuccessOrdersResult;
import com.yiyue.store.model.vo.result.getStoreOrderCountResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class StoreOrderApi {

    private interface Api {
        @GET("/store-api/storeOrder/getStoreOrderCount")
        Observable<getStoreOrderCountResult> getStoreOrderCount(@QueryMap Map<String, String> params);

        @GET("/store-api/storeOrder/getStoreSuccessOrders")
        Observable<StoreSuccessOrdersResult> getStoreSuccessOrders(@QueryMap Map<String, String> params);

        //门店订单明细查询
        @GET("/store-api/order/getListDetail")
        Observable<AllOrderDetailResult> getListDetail(@QueryMap Map<String, String> params);

        //门店时间分段笔数统计
        @GET("/store-api/storeOrder/getStoreTimeSliceOrder")
        Observable<OrderChartResult> getStoreTimeSliceOrder(@QueryMap Map<String, String> params);
    }

    private Api mApi;

    public StoreOrderApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    //所有订单信息
    public void getStoreOrderCount(String type, YLRxSubscriberHelper<getStoreOrderCountResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("storeId", AccountManager.getInstance().getStoreId());
        mApi.getStoreOrderCount(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    //获取美发师订单统计
    public void getStoreSuccessOrders(String type, String nexus, YLRxSubscriberHelper<StoreSuccessOrdersResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("storeId", AccountManager.getInstance().getStoreId());
        params.put("type", type);
        params.put("nexus", nexus);

        mApi.getStoreSuccessOrders(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);

    }

    //门店时间分段笔数统计
    public void getStoreTimeSliceOrder(String type,String storeId, YLRxSubscriberHelper<OrderChartResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("type",type);
        params.put("storeId",storeId);
        mApi.getStoreTimeSliceOrder(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);

    }

    // 门店订单明细查询
    public void getListDetail(int pageNo, int pageSize, YLRxSubscriberHelper<AllOrderDetailResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(pageNo));
        params.put("size", String.valueOf(pageSize));
        params.put("storeId", AccountManager.getInstance().getStoreId());
        mApi.getListDetail(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);

    }
}
