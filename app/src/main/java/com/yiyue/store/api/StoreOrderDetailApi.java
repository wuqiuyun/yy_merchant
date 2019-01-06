package com.yiyue.store.api;

/*
 *   美发师订单明细
 * Create by lvlong on  2018/10/26
 */


import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.OrderDetailResult;
import com.yiyue.store.model.vo.result.RegisterGapBetweenResult;
import com.yiyue.store.model.vo.result.StoreStylistResult;
import com.yiyue.store.model.vo.result.UserOrderResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class StoreOrderDetailApi {

    private interface Api {

        @GET("/store-api/storeOrderDetail/findOrderDetail")
        Observable<OrderDetailResult> findOrderDetail(@QueryMap Map<String, String> params);

        @GET("/store-api/storeOrderDetail/getRegisterGapBetween")
        Observable<RegisterGapBetweenResult> getRegisterGapBetween(@QueryMap Map<String, String> params);

        @GET("/store-api/storeOrderDetail/findOrderDetailByStoreAndStylist")
        Observable<UserOrderResult> findOrderDetailByStoreAndStylist(@QueryMap Map<String, String> params);

        @GET("/store-api/storeOrderDetail/findOrderIncomeSumByStoreAndStylist")
        Observable<StoreStylistResult> findOrderIncomeSumByStoreAndStylist(@QueryMap Map<String, String> params);



    }

    private Api mApi;

    public StoreOrderDetailApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    //订单明细
    public void findOrderDetail(int type,int pageNo,int pageSize, YLRxSubscriberHelper<OrderDetailResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("type", String.valueOf(type));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        mApi.findOrderDetail(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    //注册奖励差距
    public void getRegisterGapBetween(YLRxSubscriberHelper<RegisterGapBetweenResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.getRegisterGapBetween(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    //门店-美发师统计-用户订单列表
    public void findOrderDetailByStoreAndStylist(String stylistId,String date,int pageNo,int pageSize,YLRxSubscriberHelper<UserOrderResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("storeId", AccountManager.getInstance().getStoreId());
        params.put("stylistId", stylistId);
        params.put("date", date);
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        mApi.findOrderDetailByStoreAndStylist(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    //门店-美发师统计-用户订单统计
    public void findOrderIncomeSumByStoreAndStylist(String stylistId,String date,YLRxSubscriberHelper<StoreStylistResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("storeId", AccountManager.getInstance().getStoreId());
        params.put("stylistId", stylistId);
        params.put("date", date);
        mApi.findOrderIncomeSumByStoreAndStylist(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

}
