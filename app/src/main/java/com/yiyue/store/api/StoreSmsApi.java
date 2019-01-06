package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zm on 2018/10/15.
 */
public class StoreSmsApi {

    public interface Api {
        @POST("/store-api/storeSms/getPhoneCode")
        Observable<BaseResponse> getPhoneCode(@Body RequestBody requestBody);
        //通过本人手机号获取验证码
        @POST("/store-api/storeSms/getSelfPhoneCode")
        Observable<BaseResponse> getSelfPhoneCode(@Body RequestBody requestBody);
        //校验验证码
        @POST("/store-api/storeSms/phoneCodeCheck")
        Observable<BaseResponse> checkCode(@Body RequestBody requestBody);
    }

    private Api mApi;

    public StoreSmsApi() {
        mApi = YLRequestManager.getRequest(StoreSmsApi.Api.class);
    }

    /**
     * 获取验证码
     * @param mobile 手机号码
     * @param subscriberelper
     */
    public void getPhoneCode(String mobile, String type, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);

        mApi.getPhoneCode(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 通过本人手机号获取验证码
     * mobile (string, optional),
     * type (integer, optional),
     * userId (integer, optional)
     */
    public void getSelfPhoneCode(String mobile, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", "3");
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.getSelfPhoneCode(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 校验验证码
     * @param mobile 手机号码
     * @param subscriberelper
     */
    public void checkCode(String mobile, String type, String phoneCode, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);
        params.put("phoneCode", phoneCode);

        mApi.checkCode(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }
}
