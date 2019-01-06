package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.requestbody.UpdateAddressRequestBody;
import com.yiyue.store.model.vo.result.GetStoreCenterInfoResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 门店个人中心资料
 * <p><
 * Created by zm on 2018/10/25.
 */
public class StoreCenterInfoApi {

    public interface Api {

        // 查询门店个人资料
        @GET("/store-api/storeCenterInfo/getStoreCenterInfo")
        Observable<GetStoreCenterInfoResult> getStoreCenterInfo(@Query("id") String id);

        // 修改封面
        @POST("/store-api/storeCenterInfo/updateCoverImg")
        Observable<BaseResponse> updateCoverImg(@Body RequestBody requestBody);

        // 修改环境
        @POST("/store-api/storeCenterInfo/updateEnvironmentImg")
        Observable<BaseResponse> updateEnvironmentImg(@Body RequestBody requestBody);

        // 修改头像
        @POST("/store-api/storeCenterInfo/updateHeadImg")
        Observable<BaseResponse> updateHeadImg(@Body RequestBody requestBody);

        // 修改服务范围
        @POST("/store-api/storeCenterInfo/updateServices")
        Observable<BaseResponse> updateServices(@Body RequestBody requestBody);

        // 修改工位
        @POST("/store-api/storeCenterInfo/updateStation")
        Observable<BaseResponse> updateStation(@Body RequestBody requestBody);

        // 修改店门
        @POST("/store-api/storeCenterInfo/updateStoreName")
        Observable<BaseResponse> updateStoreName(@Body RequestBody requestBody);

        // 修改地址
        @POST("/store-api/storeCenterInfo/updateLocation")
        Observable<BaseResponse> updateLocation(@Body RequestBody requestBody);
    }

    private Api mApi;

    public StoreCenterInfoApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 查询个人资料
     */
    public void getStoreCenterInfo(YLRxSubscriberHelper<GetStoreCenterInfoResult> subscriberHelper) {
        mApi.getStoreCenterInfo(AccountManager.getInstance().getUserId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 修改封面照片
     * @param bannerPath
     * @param subscriberHelper
     */
    public void updateCoverImg(List<String> bannerPath, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, Object> params = new HashMap<>();
        params.put("imgs", bannerPath);
        params.put("storeId", AccountManager.getInstance().getStoreId());

        mApi.updateCoverImg(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 修改环境照片
     * @param imgs
     * @param subscriberHelper
     */
    public void updateEnvironmentImg(List<String> imgs, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String,Object> params = new HashMap<>();
        params.put("imgs", imgs);
        params.put("storeId", AccountManager.getInstance().getStoreId());
        mApi.updateEnvironmentImg(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 修改头像地址
     * @param headImage 头像地址
     * @param subscriberHelper
     */
    public void updateHeadImg(String headImage, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("headImg", headImage);
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.updateHeadImg(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 修改服务范围
     * @param services
     * @param subscriberHelper
     */
    public void updateServices(List<Integer> services ,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, Object> params = new HashMap<>();
        params.put("services", services);
        params.put("storeId", AccountManager.getInstance().getStoreId());
        mApi.updateServices(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 修改工位
     * @param storeStation
     * @param subscriberHelper
     */
    public void updateStation(String storeStation, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("storeStation", storeStation);
        params.put("storeId", AccountManager.getInstance().getStoreId());
        mApi.updateStation(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 修改店名
     * @param storeName
     * @param subscriberHelper
     */
    public void updateStoreName(String storeName, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("storeName", storeName);
        params.put("storeId", AccountManager.getInstance().getStoreId());
        mApi.updateStoreName(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 修改地址
     * @param requestBody
     * @param subscriberHelper
     */
    public void updateLocation(UpdateAddressRequestBody requestBody, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.updateLocation(new BaseRequestBody<>(requestBody).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);

    }
}
