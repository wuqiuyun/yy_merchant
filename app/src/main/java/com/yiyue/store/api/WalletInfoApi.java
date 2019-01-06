package com.yiyue.store.api;

/*
    我的钱包
 * Create by lvlong on  2018/10/27
 */

import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.CashInfoAccountResult;
import com.yiyue.store.model.vo.result.CashInfoResult;
import com.yiyue.store.model.vo.result.CoinInfoAccountResult;
import com.yiyue.store.model.vo.result.CoinInfoResult;
import com.yiyue.store.model.vo.result.WeiXinPayResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class WalletInfoApi {

    public interface Api{

        //获取钱包余额
        @GET("/store-api/wallet/getCashInfo")
        Observable<CashInfoResult> getCashInfo(@Query("id") String id);

        //获取代币余额
        @GET("/store-api/wallet/getCoinInfo")
        Observable<CoinInfoResult>getCoinInfo(@Query("id") String id);

        //获取钱包余额详情列表
        @GET("/store-api/wallet/getCashInfoAccount")
        Observable<CashInfoAccountResult> getCashInfoAccount(@Query("id") String id, @Query("page") String page, @Query("size") String size);

        //获取代币余额详情列表
        @GET("/store-api/wallet/getCoinInfoAccount")
        Observable<CoinInfoAccountResult>getCoinInfoAccount(@Query("id") String id, @Query("page") String page, @Query("size") String size);


        //上链
        @POST("/store-api/wallet/cochain")
        Observable<BaseResponse>cochain(@Body RequestBody requestBod);
        //转赠
        @POST("/store-api/wallet/present")
        Observable<BaseResponse>present(@Body RequestBody requestBod);
        //绑定外部钱包地址
        @POST("/store-api/wallet/bindWalletURL")
        Observable<BaseResponse>bindWalletURL(@Body RequestBody requestBod);

        //微信充值
        @POST("/store-api/wallet/wxRechargeCash")
        Observable<BaseResponse>wxRechargeCash(@Body RequestBody requestBod);

        //支付宝充值
        @POST("/store-api/wallet/aLiRechargeCash")
        Observable<BaseResponse>aLiRechargeCash(@Body RequestBody requestBod);

        //支付宝充值
        @POST()
        Observable<BaseResponse> aLiRechargeCashPost(@Url String url);

        //微信充值
        @POST()
        Observable<WeiXinPayResult> weChatCashGet(@Url String url);

        //微信提现
        @POST("/store-api/wallet/cash2Wx")
        Observable<BaseResponse>cash2Wx(@Body RequestBody requestBod);

        //支付宝提现
        @POST("/store-api/wallet/cash2ALi")
        Observable<BaseResponse>cash2ALi(@Body RequestBody requestBod);

        //ALL提现
        @POST("/store-api/wallet/toCash")
        Observable<BaseResponse>toCash(@Body RequestBody requestBod);


    }
    private WalletInfoApi.Api mApi;

    public WalletInfoApi() {
        this.mApi = YLRequestManager.getRequest(WalletInfoApi.Api.class);
    }

    /**
     * 微信充值
     */
    public void wxRechargeCash(String code,double money,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id",AccountManager.getInstance().getUserId());
        params.put("code",code);
        params.put("money",String.valueOf(money));
        mApi.wxRechargeCash(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    /**
     * 支付宝充值
     */
    public void aLiRechargeCash(double money,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id",AccountManager.getInstance().getUserId());
        params.put("money",String.valueOf(money));
        mApi.aLiRechargeCash(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 支付宝支付获取内容
     * @param url 地址
     */
    public void aLiRechargeCashPost(String url, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.aLiRechargeCashPost(url)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 微信支付获取内容
     * @param url 地址
     */
    public void weChatCashGet(String url, YLRxSubscriberHelper<WeiXinPayResult> subscriberHelper) {
        mApi.weChatCashGet(url)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }


    /**
     * 提现微信
     */
    public void cash2Wx(double money,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id",AccountManager.getInstance().getUserId());
        params.put("money",String.valueOf(money));
        mApi.cash2Wx(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 提现支付宝
     * "accountId": 提现账户ID
     * "money": 0,提现金额
     * "userId": 0
     */
    public void toCash(double money,String accountId,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId",AccountManager.getInstance().getUserId());
        params.put("money",String.valueOf(money));
        params.put("accountId",accountId);
        mApi.toCash(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 提现
     */
    public void cash2ALi(double money,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("id",AccountManager.getInstance().getUserId());
        params.put("money",String.valueOf(money));
        mApi.cash2ALi(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    //获取余额信息
    public void getCashInfo(YLRxSubscriberHelper<CashInfoResult> subscriberHelper) {
        mApi.getCashInfo(AccountManager.getInstance().getUserId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    //获取代币信息
    public void getCoinInfo(YLRxSubscriberHelper<CoinInfoResult> subscriberHelper) {
        mApi.getCoinInfo(AccountManager.getInstance().getUserId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    /**
     * 上链
     */
    public void cochain(String coinId,String num,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("coinId",coinId);
        params.put("num",num);
        mApi.cochain(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    /**
     * 转赠
     */
    public void present(String amount,String fromUserId,String toUserId,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("amount",amount);
        params.put("fromUserId",fromUserId);
        params.put("toUserId",toUserId);
        mApi.present(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    /**
     * 绑定外部钱包
     */
    public void bindWalletURL(String userId,String walletURL,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId",userId);
        params.put("walletURL",walletURL);
        mApi.present(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    //获取余额信息详情列表
    public void getCashInfoAccount(int page, int size,YLRxSubscriberHelper<CashInfoAccountResult> subscriberHelper) {
        mApi.getCashInfoAccount(AccountManager.getInstance().getUserId(),String.valueOf(page),String.valueOf(size))
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    //获取代币信息详情列表
    public void getCoinInfoAccount(int page, int size,YLRxSubscriberHelper<CoinInfoAccountResult> subscriberHelper) {
        mApi.getCoinInfoAccount(AccountManager.getInstance().getUserId(),String.valueOf(page),String.valueOf(size))
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }



}
