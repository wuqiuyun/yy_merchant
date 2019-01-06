package com.yiyue.store.api;

import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.BannerResult;
import com.yiyue.store.model.vo.result.GetStoreAttachByTypeResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zm on 2018/11/14.
 */
public class StoreAttachApi {
    private interface Api {
        @GET("store-api/storeAttach/getStoreAttachByType")
        Observable<GetStoreAttachByTypeResult> getStoreAttachByType(@Query("storeId") String storeId, @Query("type") String type);
        // banner
        @GET("store-api/storeBanner/getBanner")
        Observable<BannerResult> getBanner();
    }

    private Api mApi;

    public StoreAttachApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     *  获取门店附件
     * @param type 5封面照
     * @param subscriberHelper
     */
    public void getStoreAttachByType(String type, YLRxSubscriberHelper<GetStoreAttachByTypeResult> subscriberHelper) {
        mApi.getStoreAttachByType(AccountManager.getInstance().getStoreId(), type)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .safeSubscribe(subscriberHelper);
    }
    /**
     *  banner
     */
    public void getBanner( YLRxSubscriberHelper<BannerResult> subscriberHelper) {
        mApi.getBanner()
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .safeSubscribe(subscriberHelper);
    }


}
