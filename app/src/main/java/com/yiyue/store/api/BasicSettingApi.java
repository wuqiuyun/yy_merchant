package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.result.BsicDataResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 基础信息
 * Created by zm on 2018/11/13.
 */
public class BasicSettingApi {
    private interface Api {
        @GET("/store-api/basicSetting/getSysData")
        Observable<BsicDataResult> getSysData();
        @GET("/store-api/basicSetting/getWXShareQrCode")
        Observable<BaseResponse<String>> getWXShareQrCode(@Query("referralCode") String referralCode);
    }

    private Api mApi;

    public BasicSettingApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 获取基础信息
     * @param subscriberHelper
     */
    public void getSysData(YLRxSubscriberHelper<BsicDataResult> subscriberHelper) {
        mApi.getSysData()
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    /**
     * 生成小程序分享码
     * @param inviteCode
     * @param subscriberHelper
     */
    public void getWXShareQrCode(String inviteCode, YLRxSubscriberHelper<BaseResponse<String>> subscriberHelper) {
        mApi.getWXShareQrCode(inviteCode)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
}
