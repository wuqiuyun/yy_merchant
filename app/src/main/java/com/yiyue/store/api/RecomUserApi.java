package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.FindInviteResult;
import com.yiyue.store.model.vo.result.FindNextsResult;
import com.yiyue.store.model.vo.result.FindReCodeResult;
import com.yiyue.store.model.vo.result.FindRewardtypeResult;
import com.yiyue.store.model.vo.result.GetExplainResult;
import com.yiyue.store.model.vo.result.IncomeRecordsResult;
import com.yiyue.store.model.vo.result.RecommendResult;
import com.yiyue.store.model.vo.result.RecommendUserListResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Lizhuo on 2018/10/26.
 * 我的推荐用户
 */
public class RecomUserApi {
    
    public interface Api{
        /**
         * 绑定推荐码
         * "invitecode": "string"
         * "userId": 0
         */
        @POST("/store-api/recomUser/bindInviteCode")
        Observable<BaseResponse> bindInviteCode(@Body RequestBody requestBody);

        /**
         * 变更奖励类型
         * "changeType": "1" or "2"
         * "userId": 0
         */
        @POST("/store-api/recomUser/changeType")
        Observable<BaseResponse> changeType(@Body RequestBody requestBody);

        /**
         * 查推荐人
         * "userId": 0
         */
        @GET("/store-api/recomUser/findInvite")
        Observable<FindInviteResult> findInvite(@QueryMap Map<String, String> map);

        /**
         * 查下级被推荐
         * "page": 0
         * "size": 0
         * "userId": 0
         */
        @POST("/store-api/recomUser/findNexts")
        Observable<FindNextsResult> findNexts(@Body RequestBody requestBody);

        /**
         * 获取我的推荐码
         * "userId": 0
         */
        @GET("/store-api/recomUser/findReCode")
        Observable<FindReCodeResult> findReCode(@QueryMap Map<String, String> map);

        /**
         * 当前用户奖励类型
         * "userId": 0
         */
        @GET("/store-api/recomUser/findRewardtype")
        Observable<FindRewardtypeResult> findRewardtype(@QueryMap Map<String, String> map);

        /**
         * 推荐用户说明
         */
        @GET("/store-api/recomUser/getExplain")
        Observable<GetExplainResult> getExplain();

        /**
         * 推荐收益
         */
        @POST("/store-api/recomUser/recommend")
        Observable<RecommendResult> recommend(@Body RequestBody requestBody);

        /**
         * 推荐用户列表
         */
        @POST("/store-api/recomUser/recommendUserList")
        Observable<RecommendUserListResult> recommendUserList(@Body RequestBody requestBody);

        /**
         * 推荐收益单个用户
         * @param requestBody
         * @return
         */
        @POST("/store-api/recomUser/recommendUserIncome")
        Observable<IncomeRecordsResult> recommendUserIncome(@Body RequestBody requestBody);

        /**
         * 推荐收益列表
         * @param requestBody
         * @return
         */
        @POST("/store-api/recomUser/recommendIncomeList")
        Observable<IncomeRecordsResult> recommendIncomeList(@Body RequestBody requestBody);

        /**
         * 推荐收益列表按月筛选
         * @param requestBody
         * @return
         */
        @POST("/store-api/recomUser/recommendIncomeByMonth")
        Observable<IncomeRecordsResult> recommendIncomeByMonth(@Body RequestBody requestBody);
    }

    private Api mApi;

    public RecomUserApi() {
        mApi = YLRequestManager.getRequest(RecomUserApi.Api.class);
    }


    /**
     * 推荐收益列表
     * @param roleType
     * @param page
     * @param subscriberHelper
     */
    public void recommendIncomeList(int roleType, int page, YLRxSubscriberHelper<IncomeRecordsResult> subscriberHelper) {
        Map<String,String> params = new HashMap<>();
        params.put("roletype", String.valueOf(roleType));
        params.put("page", String.valueOf(page));
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.recommendIncomeList(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 推荐收益列表按月筛选
     * @param month
     * @param roleType
     * @param page
     * @param subscriberHelper
     */
    public void recommendIncomeByMonth(String month,int roleType, int page, YLRxSubscriberHelper<IncomeRecordsResult> subscriberHelper) {
        Map<String,String> params = new HashMap<>();
        params.put("month", month);
        params.put("roletype", String.valueOf(roleType));
        params.put("page", String.valueOf(page));
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.recommendIncomeByMonth(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 推荐单个用户收益
     * @param inviteUserId
     * @param roleType
     * @param page
     * @param subscriberHelper
     */
    public void recommendUserIncome(String inviteUserId, int roleType, int page, YLRxSubscriberHelper<IncomeRecordsResult> subscriberHelper) {
        Map<String,String> params = new HashMap<>();
        params.put("inviteUserId", inviteUserId);
        params.put("roletype", String.valueOf(roleType));
        params.put("page", String.valueOf(page));
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.recommendUserIncome(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 推荐用户列表
     * @param roletype 1门店2美发师3用户
     * @param subscriberHelper
     */
    public void recommendUserList(int roletype, YLRxSubscriberHelper<RecommendUserListResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("roletype", String.valueOf(roletype));
        mApi.recommendUserList(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 推荐收益
     * @param subscriberHelper
     */
    public void recommend(YLRxSubscriberHelper<RecommendResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.recommend(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 绑定推荐码
     * "invitecode": "string"
     * "userId": 0
     */
    public void bindInviteCode(String invitecode, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("invitecode", invitecode);
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.bindInviteCode(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 变更奖励类型
     * "changeType": "1" or "2"
     * "userId": 0
     */
    public void changeType(String changeType, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("changeType", changeType);
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.changeType(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 查推荐人
     * "userId": 0
     */
    public void findInvite(YLRxSubscriberHelper<FindInviteResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.findInvite(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 查下级被推荐
     * "page": 0
     * "size": 0
     * "userId": 0
     */
    public void findNexts(int page, int size, YLRxSubscriberHelper<FindNextsResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.findNexts(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 获取我的推荐码
     * "userId": 0
     */
    public void findReCode(YLRxSubscriberHelper<FindReCodeResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.findReCode(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 当前用户奖励类型
     * "userId": 0
     */
    public void findRewardtype(YLRxSubscriberHelper<FindRewardtypeResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.findRewardtype(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 推荐用户说明
     */
    public void getExplain(YLRxSubscriberHelper<GetExplainResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());

        mApi.getExplain()
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
}
