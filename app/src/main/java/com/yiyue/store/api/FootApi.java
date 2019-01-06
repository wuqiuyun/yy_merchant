package com.yiyue.store.api;


import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.result.OpusListResult;
import com.yiyue.store.model.vo.result.StoreListResult;
import com.yiyue.store.model.vo.result.StylistListResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Lizhuo on 2018/10/22.
 */
public class FootApi
{
    public interface Api{
        //我的足迹 作品
        @GET("/store-api/foot/getOpus")
        Observable<OpusListResult> getOpusFoot(@QueryMap Map<String, String> map);
        //我的足迹 门店
        @GET("/store-api/foot/getStore")
        Observable<StoreListResult> getStoreFoot(@QueryMap Map<String, String> map);
        //我的足迹 美发师
        @POST("/store-api/foot/getStylist")
        Observable<StylistListResult> getStylistFoot(@Body RequestBody requestBody);
    }
    
    private Api mApi;

    public FootApi()
    {
        this.mApi = YLRequestManager.getRequest(FootApi.Api.class);
    }

    /**
     * 足迹——作品列表
     * @param page 页码
     * @param size 每页数量
     * @param userId 用户id
     */
    public void getOpusFoot(int page, int size, String userId, YLRxSubscriberHelper<OpusListResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        params.put("userId", userId);

        mApi.getOpusFoot(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 足迹——门店列表
     * @param lat 经纬度
     * @param lng
     * @param page 页码
     * @param size 每页数
     * @param userId 用户id
     */
    public void getStoreFoot(double lat, double lng, int page, int size, String userId, YLRxSubscriberHelper<StoreListResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        params.put("userId", userId);

        mApi.getStoreFoot(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }


    /**
     * 足迹——美发师列表
     * @param page 页码
     * @param size 每页数量
     * @param userId 用户id
     */
    public void getStylistFoot(int page, int size, String userId, YLRxSubscriberHelper<StylistListResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        params.put("userId", userId);

        mApi.getStylistFoot(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }
}
