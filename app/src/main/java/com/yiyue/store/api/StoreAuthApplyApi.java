package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.requestbody.StoreAuthApplyRequestBody;
import com.yiyue.store.model.vo.result.GetStoreAuthApplyResult;
import com.yiyue.store.model.vo.result.StoreAuthResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 店铺认证申请api
 * <p>
 * Created by zm on 2018/10/22.
 */
public class StoreAuthApplyApi {

    public interface Api {
        // 提交认证信息
        @POST("/store-api/storeAuthApply/save")
        Observable<BaseResponse> save(@Body RequestBody requestBody);

        // 修改认证信息
        @POST("/store-api/storeAuthApply/updateOrSave")
        Observable<BaseResponse> updateOrSave(@Body RequestBody requestBody);

        // 获取认证信息OPTIONS /storeAuthApply/getStoreAuthStatusByStoreId
        @GET("/store-api/storeAuthApply/getStoreAuthStatusByStoreId")
        Observable<GetStoreAuthApplyResult> getStoreAuthApplyByStoreId(@Query("storeId")String storeId);

        // 获取门店认证信息
        @GET("/store-api/storeAuthApply/getStoreAuthDTO")
        Observable<StoreAuthResult> getStoreAuthDTO(@Query("storeId")String storeId);



    }

    private Api mApi;

    public StoreAuthApplyApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 提交认证信息
     * @param requestBody 认证信息
     * @param subscriberHelper
     */
    public void save(StoreAuthApplyRequestBody requestBody, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.save(new BaseRequestBody(requestBody).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 获取认证信息
     * @param subscriberHelper
     */
    public void getStoreAuthApplyByStoreId(YLRxSubscriberHelper<GetStoreAuthApplyResult> subscriberHelper) {
        mApi.getStoreAuthApplyByStoreId(AccountManager.getInstance().getStoreId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 获取门店认证信息
     * @param subscriberHelper
     */
    public void getStoreAuthDTO(YLRxSubscriberHelper<StoreAuthResult> subscriberHelper) {
        mApi.getStoreAuthDTO(AccountManager.getInstance().getStoreId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

}
