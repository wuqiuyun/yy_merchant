package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.result.GetSystemNoticeResult;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 系统相关api
 * <p>
 * Created by zm on 2018/9/9.
 */
public class SysApi {
    public interface Api {
        @POST("/store-api/jjsd/sys/notice")
        Observable<GetSystemNoticeResult> getNoticeList(@Body RequestBody requestBody);
    }

    private Api api;

    public SysApi() {
        this.api = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 获取系统公告
     * @param lastId
     */
    public void getNoticeList(String lastId, YLRxSubscriberHelper<GetSystemNoticeResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("lastId", lastId);
        api.getNoticeList(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }
}
