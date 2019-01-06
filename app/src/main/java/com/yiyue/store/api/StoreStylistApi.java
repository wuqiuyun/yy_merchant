package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.CheckStoreResult;
import com.yiyue.store.model.vo.result.OpusDetailResult;
import com.yiyue.store.model.vo.result.OpusListResult;
import com.yiyue.store.model.vo.result.StoreInfoResult;
import com.yiyue.store.model.vo.result.StylistCardResult;
import com.yiyue.store.model.vo.result.StylistListResult;
import com.yiyue.store.model.vo.result.StylistOpusListResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Lizhuo on 2018/10/24.
 * 美发师相关接口
 */
public class StoreStylistApi {
    public interface Api {
        @POST("/store-api/storeStylist/card")
        Observable<StylistCardResult> card(@Body RequestBody requestBody);

        @POST("/store-api/storeStylist/inviteNear")
        Observable<StylistListResult> inviteNear(@Body RequestBody requestBody);

        @POST("/store-api/storeStylist/inviteScreen")
        Observable<StylistListResult> inviteScreen(@Body RequestBody requestBody);

        @POST("/store-api/storeStylist/inviteSearch")
        Observable<StylistListResult> inviteSearch(@Body RequestBody requestBody);

        //我的美发师-综合排序
        @POST("/store-api/storeStylist/inviteSort")
        Observable<StylistListResult> inviteSort(@Body RequestBody requestBody);

        @POST("/store-api/storeStylist/opusDetail")
        Observable<OpusDetailResult> opusDetail(@Body RequestBody requestBody);

        @POST("/store-api/storeStylist/opusList")
        Observable<StylistOpusListResult> opusList(@Body RequestBody requestBody);

        // 美发师作品集筛选
        @POST("/store-api/storeStylist/opusListScreen")
        Observable<StylistOpusListResult> opusListScreen(@Body RequestBody requestBody);

        @POST("/store-api/storeStylist/oupsCollection")
        Observable<BaseResponse> oupsCollection(@Body RequestBody requestBody);

        @POST("/store-api/storeStylist/oupsCount")
        Observable<BaseResponse> oupsCount(@Body RequestBody requestBody);

        @POST("/store-api/store/getStoreInforamtion")
        Observable<StoreInfoResult> getStoreInforamtion(@Body RequestBody requestBody);

        @POST("/store-api/store/nexus")
        Observable<BaseResponse> nexus(@Body RequestBody requestBody);//门店端签约或者入驻美发师接口

        @POST("/store-api/store/refuse")
        Observable<BaseResponse> refuse(@Body RequestBody requestBody);//门店端拒绝 签约或者入驻美发师接口

        @POST("/store-api/store/checkStoreAuth")
        Observable<CheckStoreResult> checkStoreAuth(@Body RequestBody requestBody);//获取门店 是否已认证

    }

    private Api api;

    public StoreStylistApi() {
        this.api = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 获取美发师名片 stylistId必传，其他不要传
     *
     * @param nexus
     * @param storeId   门店id
     * @param stylistId 美发师id
     */
    public void getStylistCardList(String nexus, String storeId, String stylistId, YLRxSubscriberHelper<StylistCardResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        if (null != nexus) params.put("nexus", nexus);
        if (null != storeId) params.put("storeId", storeId);
        params.put("stylistId", stylistId);

        api.card(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 邀请附近美发师
     * storeId必须传，distance,cityId,districtId必须传一个，三个都传优先顺序为distance,cityId,districtId
     *
     * @param cityId
     * @param districtId
     * @param distance
     * @param storeId
     */
    public void inviteNear(String cityId, String districtId, String distance, String storeId, int page, YLRxSubscriberHelper<StylistListResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        if (null != cityId) params.put("cityId", cityId);
        if (null != districtId) params.put("districtId", districtId);
        if (null != distance) params.put("distance", distance);
        params.put("storeId", storeId);
        params.put("page", String.valueOf(page));

        api.inviteNear(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 筛选美发师
     *
     * @param coupon   优惠券、套餐券
     * @param position 美发师等级
     * @param page
     * @param storeId
     */
    public void inviteScreen(String coupon, String position, int page, String storeId, YLRxSubscriberHelper<StylistListResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        if (!coupon.equals("-1")) {
            params.put("coupon", coupon);
        }
        if (!position.equals("-1")) {
            params.put("position", position);
        }
        params.put("page", String.valueOf(page));
        params.put("storeId", storeId);

        api.inviteScreen(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 根据昵称搜索美发师
     *
     * @param nickName
     * @param page
     * @param storeId
     */
    public void inviteSearch(String nickName, int page, String storeId, YLRxSubscriberHelper<StylistListResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nickName", nickName);
        params.put("page", String.valueOf(page));
        params.put("storeId", storeId);

        api.inviteSearch(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 美发师按条件排序
     *
     * @param sortType 排序条件
     * @param page
     * @param storeId
     */
    public void inviteSort(int sortType, int page, String storeId, YLRxSubscriberHelper<StylistListResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("sortType", String.valueOf(sortType));
        params.put("page", String.valueOf(page));
        params.put("storeId", storeId);

        api.inviteSort(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 获取美发师作品详情
     *
     * @param opusId
     * @param userId
     */
    public void opusDetail(String opusId, String userId, YLRxSubscriberHelper<OpusDetailResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("opusId", opusId);
        params.put("userId", userId);

        api.opusDetail(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 美发师作品集
     * stylistId必传，其他不要传
     *
     * @param stylistId
     */
    public void opusList(String stylistId, YLRxSubscriberHelper<StylistOpusListResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("stylistId", stylistId);
        params.put("storeId", AccountManager.getInstance().getStoreId());

        api.opusList(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 筛选美发师作品
     *
     * @param stylistId
     * @param screenId 对应筛选项的id
     * @param type  筛选类型 1发类  2脸型
     */
    public void getOpusListScreen(String stylistId, String screenId,String type, YLRxSubscriberHelper<StylistOpusListResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("screenId", screenId);
        params.put("stylistId", stylistId);
        params.put("type", type);

        api.opusListScreen(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 收藏/取消收藏美发师作品
     *
     * @param opusId
     * @param type
     * @param userId
     */
    public void oupsCollection(String opusId, String type, String userId, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("opusId", opusId);
        params.put("type", type);
        params.put("userId", userId);

        api.oupsCollection(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 转发，浏览美发师作品
     *
     * @param opusId
     * @param type
     */
    public void oupsCount(String opusId, String type, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("opusId", opusId);
        params.put("type", type);

        api.oupsCount(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 门店信息接口
     *
     * @param storeId
     * @param rxSubscriberHelper
     */
    public void getStoreInforamtion(String storeId, YLRxSubscriberHelper<StoreInfoResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("storeId", storeId);

        api.getStoreInforamtion(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }


    /**
     * 门店端签约或者入驻美发师接口
     * nexus": 0,
     * "storeId": 0,
     * "stylistId": 0
     */
    public void nexus(String nexus, String storeId, String stylistId, String msgId, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nexus", nexus);
        params.put("storeId", storeId);
        params.put("stylistId", stylistId);
        params.put("msgId", msgId);

        api.nexus(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 门店端拒绝  签约或者入驻美发师接口
     * nexus": 0,
     * "storeId": 0,
     * "stylistId": 0
     *
     *     * "storeId": 0, 门店用户ID
     * "stylistId": 0 美发师用户ID
     *     签约类型0入驻，1签约
     */
    public void refuse(String type, String storeId, String stylistId, String msgId, YLRxSubscriberHelper<BaseResponse> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("storeUserId", storeId);
        params.put("stylistUserId", stylistId);
        params.put("msgId", msgId);

        api.refuse(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 判断当前门店是否已经认证
     * @param storeId
     * @param rxSubscriberHelper
     */
    public void checkStoreAuth(String storeId, YLRxSubscriberHelper<CheckStoreResult> rxSubscriberHelper) {
        HashMap<String, String> params = new HashMap<>();
        params.put("storeId", storeId);

        api.checkStoreAuth(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }
}
