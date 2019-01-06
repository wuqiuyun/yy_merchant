package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.result.GetCategoryResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 获取平台类目
 * <p>
 * Created by zm on 2018/10/27.
 */
public class CategoryApi {

    private interface Api {
        @POST("/store-api/category/getAll")
        Observable<GetCategoryResult> getAll(@Body RequestBody requestBody);
    }

    private Api mApi;

    public CategoryApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 获取平台所有类目
     * @param subscriberHelper
     */
    public void getAll(YLRxSubscriberHelper<GetCategoryResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        mApi.getAll(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
}
