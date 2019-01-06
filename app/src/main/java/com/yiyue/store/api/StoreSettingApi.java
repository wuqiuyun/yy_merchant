package com.yiyue.store.api;

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.model.vo.result.StoreSettingTimeResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 店鋪設置相关api
 * <p>
 * Created by lyj on 2018/10/22.
 */

public class StoreSettingApi {
    public interface Api {
        /**
         * 门店时间管理 :
         *
         * 根据店铺ID查询
         * */
        @GET("/store-api/storeSetting/getStoreSettingByStoreId")
        Observable<StoreSettingTimeResult> getStoreSettingByStoreId(@Query("storeId") String storeId);

        /**
         * 门店时间管理 :
         *
         * updateOrSave门店时间
         * */
        @POST("/store-api/storeSetting/updateOrSave")
        Observable<StoreSettingTimeResult> updateOrSave(@Body RequestBody requestBody);

    }

    private Api mApi;

    public StoreSettingApi() {
        this.mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     *  根据店铺ID查询
     *
     * //@param storeId 门店ID
     */
    public void getStoreSettingByStoreId(String storeId,YLRxSubscriberHelper<StoreSettingTimeResult> subscriberHelper) {

        mApi.getStoreSettingByStoreId(storeId)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     *  updateOrSave门店时间
     *  @param open 开店，默认1 关店0
     * @param starttime 开始时间
     * @param endtime 结束时间
     * @param station 工位
     * @param stationlock 工位锁定
     * @param storeId 门店ID
     * @param workday 营业日 1,2,3,4,5,6,7
     * @param id
     */
    public void updateOrSave(int open, String starttime, String endtime, int station,
                             int stationlock, String storeId, String workday, int id, YLRxSubscriberHelper<StoreSettingTimeResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("endtime", endtime);
        params.put("id", String.valueOf(id));
        params.put("open", String.valueOf(open));
        params.put("starttime", starttime);
        params.put("station", String.valueOf(station));
        params.put("stationlock", String.valueOf(stationlock));
        params.put("storeId", storeId);
        params.put("workday", workday);

        mApi.updateOrSave(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

}
