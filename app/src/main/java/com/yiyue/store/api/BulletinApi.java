package com.yiyue.store.api;

import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.BulletinDetailResult;
import com.yiyue.store.model.vo.result.BulletinListResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lizhuo on 2018/11/19.
 * 公告相关Api
 */
public class BulletinApi {

    public interface Api {

        //公告详情
        @GET("/msg-api/bulletin/findBulletin")
        Observable<BulletinDetailResult> findBulletin(@Query("id") String id, @Query("userId") String userId);
        //公告列表
        @GET("/msg-api/bulletin/findPageList")
        Observable<BulletinListResult> findPageList(@Query("pageNo") String pageNo, @Query("pageSize") String pageSize, @Query("userId") String userId);

    }

    private Api api;

    public BulletinApi() {
        this.api = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 公告详情
     * @param id
     */
    public void findBulletin(String id, YLRxSubscriberHelper<BulletinDetailResult> rxSubscriberHelper){
        api.findBulletin(id, AccountManager.getInstance().getUserId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 公告列表
     * @param pageNo
     * @param pageSize
     */
    public void findPageList(String pageNo, String pageSize, YLRxSubscriberHelper<BulletinListResult> rxSubscriberHelper){
        api.findPageList(pageNo, pageSize, AccountManager.getInstance().getUserId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }
}
