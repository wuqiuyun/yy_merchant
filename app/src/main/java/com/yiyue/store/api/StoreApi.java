package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.requestbody.CommitSetServiceBody;
import com.yiyue.store.model.vo.result.CheckMsgResult;
import com.yiyue.store.model.vo.result.GetStoreOrderStatisticalResult;
import com.yiyue.store.model.vo.result.SendMsgResult;
import com.yiyue.store.model.vo.result.StoreCollectionResult;
import com.yiyue.store.model.vo.result.StoreSettinResult;
import com.yiyue.store.model.vo.result.StylistListResult;
import com.yiyue.store.model.vo.result.StylistNumResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zm on 2018/10/24.
 */
public class StoreApi {

   public interface Api {
       // 获取订单统计
       @GET("/store-api/store/getStoreOrderStatistical")
       Observable<GetStoreOrderStatisticalResult> getStoreOrderStatistical(@Query("storeId") String storeId);

       // 获取我的美发师列表
       @POST("/store-api/store/storeStylist")
       Observable<StylistListResult> getMyStylist(@Body RequestBody requestBody);


       // 门店收藏和取消收藏美发师
       @POST("/store-api/store/storeCollection")
       Observable<StoreCollectionResult> getStoreCollection(@Body RequestBody requestBody);

       // 设置服务
       @POST("/store-api/store/setting")
       Observable<BaseResponse> setting(@Body RequestBody requestBody);
       // 获取服务设置
       @POST("/store-api/store/getstoreSetting")
       Observable<StoreSettinResult> getStoreSetting(@Body RequestBody requestBody);

       //门店与美发师解约接口
       @POST("/store-api/store/breakStoreNexus")
       Observable<BaseResponse> breakStoreNexus(@Body RequestBody requestBody);

       @POST("/store-api/store/getStoreStylistNumber")
       Observable<StylistNumResult> getStoreStylistNumber(@Body RequestBody requestBody);

       @POST("/store-api/store/storeStylistSearch")
       Observable<StylistListResult> storeStylistSearch(@Body RequestBody requestBody);

       @POST("/store-api/store/checkMsg")
       Observable<CheckMsgResult> checkMsg(@Body RequestBody requestBody);

       @POST("/store-api/store/sendMsg")
       Observable<SendMsgResult> sendMsg(@Body RequestBody requestBody);

   }

   private Api mApi;

    public StoreApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 获取订单统计
     * @param subscriberHelper
     */
    public void getStoreOrderStatistical(YLRxSubscriberHelper<GetStoreOrderStatisticalResult> subscriberHelper) {
        mApi.getStoreOrderStatistical(AccountManager.getInstance().getStoreId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 获取我的美发师列表
     * @param nexus 签约0,入驻1
     * @param storeId 门店ID
     */
    public void getMyStylist(int nexus, String storeId, YLRxSubscriberHelper<StylistListResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("nexus", String.valueOf(nexus));
        params.put("storeId", storeId);

        mApi.getMyStylist(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 获取美发师名片 stylistId必传，其他不要传
     * @param stylistId 美发师I
     * @param type 收藏类型1收藏0取消 ,
     *  userId 用户ID
     */

    public void getStoreCollection(String stylistId, int type, YLRxSubscriberHelper<StoreCollectionResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("stylistId", stylistId);
        params.put("type", String.valueOf(type));
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.getStoreCollection(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }
    /**
     * 提交设置服务
     */
    public void setting(CommitSetServiceBody commitSetServiceBody, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        mApi.setting(new BaseRequestBody(commitSetServiceBody).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 提交设置服务
     */
    public void getStoreSetting(String storeId, YLRxSubscriberHelper<StoreSettinResult> subscriberelper) {

        Map<String, String> params = new HashMap<>();
        params.put("storeId", storeId);
        mApi.getStoreSetting(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 门店与美发师解约
     * @param nexus 签约0,入驻1
     * @param storeId 门店ID
     * @param stylistId (integer, optional): 美发师ID
     */
    public void breakStoreNexus(int nexus, String storeId, String stylistId, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("nexus", String.valueOf(nexus));
        params.put("storeId", storeId);
        params.put("stylistId", stylistId);

        mApi.breakStoreNexus(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /*
        获取门店平台美发师和店内美发师的数量
     */

    public void getStoreStylistNumber(YLRxSubscriberHelper<StylistNumResult> subscriberHelper){
        Map<String, String> params = new HashMap<>();
        params.put("storeId" , AccountManager.getInstance().getStoreId());

        mApi.getStoreStylistNumber(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 搜索我的美发师
     * @param nickName
     * @param page
     * @param storeId
     * @param rxSubscriberHelper
     */
    public void storeStylistSearch(String nickName, int page, String storeId, YLRxSubscriberHelper<StylistListResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", nickName);
        params.put("page", String.valueOf(page));
        params.put("storeId", storeId);
        mApi.storeStylistSearch(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 消息处理状态
     * @param msgId 入驻申请消息id
     * @param rxSubscriberHelper
     */
    public void checkMsg(String msgId, YLRxSubscriberHelper<CheckMsgResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("msgId", msgId);
        mApi.checkMsg(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     *
     * @param storeId
     * @param stylistId
     * @param nexus 签约类型0入驻平台，1签约门店
     * @param rxSubscriberHelper
     */
    public void sendMsg(String storeId, String stylistId, int nexus, YLRxSubscriberHelper<SendMsgResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("storeId", storeId);
        params.put("stylistId", stylistId);
        if (nexus == 111){ //平台
            params.put("nexus", "0");
        } else {
            params.put("nexus", "1");
        }

        mApi.sendMsg(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }
}
