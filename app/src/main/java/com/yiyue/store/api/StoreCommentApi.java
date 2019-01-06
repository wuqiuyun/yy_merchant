package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.StoreCommentListResult;
import com.yiyue.store.model.vo.result.StoreManageScopeResult;
import com.yiyue.store.model.vo.result.StylistEvaluationResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 评价管理api
 * <p>
 * Created by lyj on 2018/10/23.
 */

public class StoreCommentApi {
    public interface Api {

        /**
         * 评价评分
         * <p>
         * storeId (integer, optional): 门店id（非本人门店必传） ,
         * userId (integer, optional): 用户id
         */
        @GET("/store-api/storeComment/getStoreComment")
        Observable<StoreManageScopeResult> getStoreComment(@Query("userId") String userId, @Query("storeId") String storeId);

        /**
         * 门店顾客评价
         * <p>
         * storeId (integer, optional): 门店ID，非本人门店必传 ,
         * userId (integer, optional): 用户id
         */
        @GET("/store-api/storeManage/getStoreScore")
        Observable<StoreManageScopeResult> getStoreScore(@Query("userId") String userId, @Query("storeId") String storeId);

        /**
         * 回复客户评论
         * <p>
         * context  (number, optional): 内容 ,
         * storeId (integer, optional): 门店id ,
         * userId (integer, optional): 用户id
         */
        @POST("/store-api/storeComment/replyStoreComment")
        Observable<BaseResponse> replyStoreComment(@Body RequestBody requestBody);

        /**
         * 门店顾客评价列表
         * <p>
         * page 页数
         * id (integer, optional): 用户id
         * size (integer, optional): 每一页的大小
         */
        @GET("/store-api/storeComment/getStoreCommentList")
        Observable<StoreCommentListResult> getStoreCommentList(@Query("storeId") String storeId, @Query("page") String page, @Query("size") String size);

        //查看美发师评分
        @GET("/store-api/stylistComment/getEvaluate")
        Observable<StylistEvaluationResult> getEvaluate(@Query("stylistId") String stylistId);

        //查看美发师评论
        @GET("/store-api/stylistComment/getStylistCommentList")
        Observable<StoreCommentListResult> getStylistCommentList(@Query("stylistId") String stylistId, @Query("page") String page, @Query("size") String size);
    }


    private Api mApi;

    public StoreCommentApi() {
        this.mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 回复客户评论
     * <p>
     * context (string, optional): 内容
     * storeId (integer, optional): 门店id（非本人门店必传） ,
     * userId (integer, optional): 用户id
     */
    public void replyStoreComment(String context, String storeReviewsId,
                                  YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("storeReviewsId", storeReviewsId);
        params.put("context", context);

        mApi.replyStoreComment(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 评价
     * <p>
     * storeId (integer, optional): 门店id（非本人门店必传） ,
     * userId (integer, optional): 用户id
     */
    public void getStoreScore(String storeId,
                              YLRxSubscriberHelper<StoreManageScopeResult> subscriberHelper) {

        mApi.getStoreScore(AccountManager.getInstance().getUserId(), storeId)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 门店顾客评价列表
     * <p>
     * page 页数
     * id (integer, optional): 用户id
     * size (integer, optional): 每一页的大小
     */
    public void getStoreCommentList(String storeId,int page, int size, YLRxSubscriberHelper<StoreCommentListResult> subscriberHelper) {

        mApi.getStoreCommentList(storeId, String.valueOf(page), String.valueOf(size))
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    public void getEvaluate(String stylitId, YLRxSubscriberHelper<StylistEvaluationResult> subscriberHelper) {

        mApi.getEvaluate(stylitId)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    public void getStylistCommentList(String stylitId, int page, int size,
                                      YLRxSubscriberHelper<StoreCommentListResult> subscriberHelper) {

        mApi.getStylistCommentList(stylitId, String.valueOf(page), String.valueOf(size))
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);

    }
}
