package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.result.GetAreaResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by zm on 2018/10/25.
 */
public class AreaApi {
    public interface Api {
        // 省市区
        @GET("/store-api/area/getArea")
        Observable<GetAreaResult> getArea();
        // 通过门店ID获取区域
        @POST("/store-api/area/getAreaByStoreId")
        Observable<GetAreaResult> getAreaByStoreId(@Body RequestBody requestBody);
    }

    private Api mApi;

    public AreaApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 获取省市区数据
     */
    public void getArea(YLRxSubscriberHelper<GetAreaResult> subscriberHelper) {
        mApi.getArea()
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    /**
     * 获取省市区数据
     */
    public void getAreaByStoreId(String storeId,YLRxSubscriberHelper<GetAreaResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("storeId",storeId);
        mApi.getAreaByStoreId(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
}
