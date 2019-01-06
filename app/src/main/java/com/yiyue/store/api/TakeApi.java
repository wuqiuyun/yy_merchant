package com.yiyue.store.api;


import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.BankDetailsResult;
import com.yiyue.store.model.vo.result.BankSumResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wqy on 2018/12/20.
 */

public class TakeApi {
    public interface Api {
        // 各银行提现账单总计
        @GET("/store-api/take/getBankSum")
        Observable<BankSumResult> getBankSum(@Query("userId") String userId, @Query("month") String month);

        // 当前银行提现记录
        @POST("/store-api/take/showBanks")
        Observable<BankDetailsResult> showBanks(@Body RequestBody requestBody);

        // 显示最近提现两个账户总计
        @GET("/store-api/take/getNewesTwo")
        Observable<BankSumResult> getNewesTwo(@Query("userId") String userId, @Query("month") String month);

        // 当前银行提现记录的和
        @POST("/store-api/take/showBanksSum")
        Observable<BankDetailsResult> showBanksSum(@Body RequestBody requestBody);
    }

    private Api mApi;

    public TakeApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 各银行提现账单总计
     *
     * @param month
     * @param subscriberHelper
     */
    public void getBankSum(String month, YLRxSubscriberHelper<BankSumResult> subscriberHelper) {
        mApi.getBankSum(AccountManager.getInstance().getUserId(), month)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 当前银行提现记录
     *
     * @param month
     * @param shortName
     * @param page
     * @param size
     * @param subscriberHelper
     */
    public void showBanks(String month, String shortName, String accountno, int page, int size, YLRxSubscriberHelper<BankDetailsResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("month", month);
        params.put("page", String.valueOf(page));
        params.put("shortName", shortName);
        params.put("accountno", accountno);
        params.put("size", String.valueOf(size));
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.showBanks(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 显示最近提现两个账户总计
     *
     * @param month
     * @param subscriberHelper
     */
    public void getNewesTwo(String month, YLRxSubscriberHelper<BankSumResult> subscriberHelper) {
        mApi.getNewesTwo(AccountManager.getInstance().getUserId(), month)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 当前银行提现记录和
     *
     * @param month
     * @param shortName
     * @param page
     * @param size
     * @param subscriberHelper
     */
    public void showBanksSum(String month, String shortName, String accountno, int page, int size, YLRxSubscriberHelper<BankDetailsResult> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("month", month);
        params.put("page", String.valueOf(page));
        params.put("shortName", shortName);
        params.put("accountno", accountno);
        params.put("size", String.valueOf(size));
        params.put("userId", AccountManager.getInstance().getUserId());
        mApi.showBanksSum(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

}
