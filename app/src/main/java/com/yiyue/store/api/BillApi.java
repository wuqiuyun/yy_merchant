package com.yiyue.store.api;


import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.BillDetailsResult;
import com.yiyue.store.model.vo.result.MonthSumResult;
import com.yiyue.store.model.vo.result.TotalBillResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * 账单
 */
public class BillApi {
    private interface Api {
        @GET("/store-api/bill/getMonthSum")
        Observable<MonthSumResult> getMonthSum(@QueryMap Map<String, String> params);

        @GET("/store-api/bill/getMonthSumIn")
        Observable<MonthSumResult> getMonthSumIn(@QueryMap Map<String, String> params);

        @GET("/store-api/bill/getMonthSumOut")
        Observable<MonthSumResult> getMonthSumOut(@QueryMap Map<String, String> params);

        @POST("/store-api/bill/getBill")
        Observable<TotalBillResult> getBill(@Body RequestBody requestBody);

        @POST("/store-api/bill/getDetail")
        Observable<BillDetailsResult> getDetail(@Body RequestBody requestBody);
    }

    private Api mApi;

    public BillApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    //账单总计
    public void getMonthSum(String month, YLRxSubscriberHelper<MonthSumResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("month", month);
        mApi.getMonthSum(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    //收入总计
    public void getMonthSumIn(String month, String type, YLRxSubscriberHelper<MonthSumResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("month", month);
        params.put("type", type);
        mApi.getMonthSumIn(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    //支出总计
    public void getMonthSumOut(String month, YLRxSubscriberHelper<MonthSumResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("month", month);
        mApi.getMonthSumOut(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 收入/支出记录
     *
     * @param month            月份，格式如：2018-12
     * @param page
     * @param size
     * @param type             1:收入，2:支出
     * @param subscriberHelper
     */
    public void getBill(String month, int page, int size, int type, YLRxSubscriberHelper<TotalBillResult> subscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("month", month);
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        params.put("type", String.valueOf(type));
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.getBill(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 账单详情
     * @param billId
     * @param inType
     * @param type
     * @param subscriberHelper
     */
    public void getDetail(String billId, int inType, int type, YLRxSubscriberHelper<BillDetailsResult> subscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("billId", billId);
        params.put("inType", String.valueOf(inType));
        params.put("type", String.valueOf(type));
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.getDetail(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

}
