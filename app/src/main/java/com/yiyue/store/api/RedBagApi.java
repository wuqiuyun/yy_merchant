package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.result.RedBagSendResult;
import com.yiyue.store.model.vo.result.RedRecordResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by zhangzz on 2018/11/9.
 */
public class RedBagApi {
    public interface Api {
        // 查询红包详情
        @GET("/msg-api/RedBag/findRedBag")
        Observable<RedBagSendResult> findRedBag(@QueryMap Map<String, String> params);

        @GET("/msg-api/RedBag/findRedBagLog")
        Observable<RedRecordResult> findRedBagLog(@QueryMap Map<String, String> params);

        @GET("/msg-api/RedBag/findTransfer")
        Observable<RedBagSendResult> findTransfer(@QueryMap Map<String, String> params);

        @GET("/msg-api/RedBag/findTransferLog")
        Observable<RedRecordResult> findTransferLog(@QueryMap Map<String, String> params);

        // 发红包
        @POST("/msg-api/RedBag/sendRedBag")
        Observable<RedBagSendResult> sendRedBag(@Body RequestBody requestBody);

        // 发转账
        @POST("/msg-api/RedBag/sendTransfer")
        Observable<RedBagSendResult> sendTransfer(@Body RequestBody requestBody);

        // 接收红包
        @POST("/msg-api/RedBag/receiveRedBag")
        Observable<RedBagSendResult> receiveRedBag(@Body RequestBody requestBody);

        // 接收红包
        @POST("/msg-api/RedBag/receiveTransfer")
        Observable<RedBagSendResult> receiveTransfer(@Body RequestBody requestBody);

    }

    private Api mApi;

    public RedBagApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 查询红包
     */
    public void findRedBag(String id, YLRxSubscriberHelper<RedBagSendResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.findRedBag(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 查询红包记录
     */
    public void findRedBagLog(String userId, String status, String pageNo, String pageSize, YLRxSubscriberHelper<RedRecordResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", status);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);

        mApi.findRedBagLog(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 查询转账
     */
    public void findTransfer(String id, YLRxSubscriberHelper<RedBagSendResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.findTransfer(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 查询转账记录
     */
    public void findTransferLog(String userId, String status, String pageNo, String pageSize, YLRxSubscriberHelper<RedRecordResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", status);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);

        mApi.findTransferLog(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 发送红包
     */
    public void sendRedBag(String receiveUserId, String remark, String sendUserId, String sendamount, YLRxSubscriberHelper<RedBagSendResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("receiveUserId", receiveUserId);
        params.put("remark", remark);
        params.put("sendUserId", sendUserId);
        params.put("sendamount", sendamount);
        mApi.sendRedBag(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 发送转账
     */
    public void sendTransfer(String receiveUserId, String remark, String sendUserId, String sendamount, YLRxSubscriberHelper<RedBagSendResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("receiveUserId", receiveUserId);
        params.put("remark", remark);
        params.put("sendUserId", sendUserId);
        params.put("sendamount", sendamount);
        mApi.sendTransfer(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 接收红包
     */
    public void receiveRedBag(String id, YLRxSubscriberHelper<RedBagSendResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.receiveRedBag(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 接收转账
     */
    public void receiveTransfer(String id, YLRxSubscriberHelper<RedBagSendResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        mApi.receiveTransfer(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
}
