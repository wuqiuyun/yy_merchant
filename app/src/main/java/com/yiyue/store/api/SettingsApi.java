package com.yiyue.store.api;

import com.yiyue.store.BuildConfig;
import com.yiyue.store.base.data.BaseRequestBody;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.BankCardResult;
import com.yiyue.store.model.vo.result.CashAliResult;
import com.yiyue.store.model.vo.result.FeedBackResult;
import com.yiyue.store.model.vo.result.GetAppInfoResult;
import com.yiyue.store.model.vo.result.SecurityInfoResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zm on 2018/10/25.
 */
public class SettingsApi {
    public interface Api {
        // 体验反馈
        @POST("/store-api/settings/submitFeedback")
        Observable<BaseResponse> submitFeedback(@Body RequestBody requestBody);
        // 体验反馈信息
        @GET("/store-api/settings/initAppFeedback")
        Observable<FeedBackResult> initAppFeedback(@Query("client") String client);
        // 变更账户密码（知道原密码）
        @POST("/store-api/settings/accountSafe/updatePassword")
        Observable<BaseResponse> upDatePwd(@Body RequestBody requestBody);
        // 变更手机号码
        @POST("/store-api/settings/accountSafe/updateMobile")
        Observable<BaseResponse> updateMobile(@Body RequestBody requestBody);
        // 版本更新
        @POST("/store-api/settings/getAppInfos")
        Observable<GetAppInfoResult> getAppInfos(@Body RequestBody requestBody);
        // 账户安全信息查询
        @GET("/store-api/settings/accountSafe/find")
        Observable<SecurityInfoResult> accountSafeInfo(@Query("userId") String userId);
        // 绑定微信
        @POST("/store-api/settings/bindWX")
        Observable<BaseResponse> bindWXAccount(@Body RequestBody requestBody);
        //验证密码
        @GET("/store-api/settings/accountSafe/checkPassword")
        Observable<BaseResponse> checkPassword(@Query("userId") String userId, @Query("password") String password);

        // 绑定提现账户
        @POST("/store-api/settings/bindAccount")
        Observable<BaseResponse> bindAccount(@Body RequestBody requestBody);
        // 查询当前绑定提现账户
        @GET("/store-api/settings/extractAccount")
        Observable<CashAliResult> extractAccount(@Query("userId") String userId, @Query("bindType") String bindType);
        // 所有银行
        @GET("/store-api/settings/getAllBank")
        Observable<BankCardResult> getAllBank();
        // 解除绑定的账户
        @POST("/store-api/settings/unBind")
        Observable<BaseResponse> unBind(@Body RequestBody requestBody);

        // 获取支付密码状态
        @GET("/store-api/settings/initPayWord")
        Observable<BaseResponse> initPayWord(@Query("userId") String userId);

        // 验证支付密码
        @POST("/store-api/settings/checkPayWord")
        Observable<BaseResponse> checkPayWord(@Body RequestBody requestBody);

        // 身份证号验证
        @GET("/store-api/settings/checkIDcard")
        Observable<BaseResponse> checkIDcard(@Query("userId") String userId,@Query("cardNo") String cardNo);

        // 设置支付密码
        @POST("/store-api/settings/setPaypassword")
        Observable<BaseResponse> setPaypassword(@Body RequestBody requestBody);
        
        // 设置是否接受新消息通知
        @GET("/store-api/settings/changeNotice")
        Observable<BaseResponse> changeNotice(@Query("userId") String userId,@Query("shutdown") String shutdown);

    }

    private Api mApi;

    public SettingsApi() {
        mApi = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 设置是否接受新消息通知
     * @param shutdown 0通知，1不通知
     */
    public void changeNotice(int shutdown, YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.changeNotice(AccountManager.getInstance().getUserId(),String.valueOf(shutdown))
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }
    
    /**
     * 获取支付密码状态
     * @param subscriberHelper
     */
    public void initPayWord(YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.initPayWord(AccountManager.getInstance().getUserId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 身份证号验证
     * @param subscriberHelper
     */
    public void checkIDcard(String cardNo,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        mApi.checkIDcard(AccountManager.getInstance().getUserId(),cardNo)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 支付密码验证
     * @param subscriberelper
     * payWord (string, optional): 支付密码 ,
     * userId (integer, optional): 用户ID
     */
    public void checkPayWord(String payWord, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("payWord", payWord);

        mApi.checkPayWord(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 支付密码设置
     * @param subscriberelper
     * copyPayword (string, optional): 确认支付密码 ,
     * payword (string, optional): 支付密码 ,
     * userId (integer, optional): 用户ID
     */
    public void setPaypassword(String payword,String copyPayword, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("payword", payword);
        params.put("copyPayword", copyPayword);

        mApi.setPaypassword(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 绑定提现账户
     * @param subscriberelper
     * accountNo (string, optional): 绑定账户 ,
     * bindType (string, optional): 账户类型，ALI(支付宝)，BANK(银行) ,
     * branch (string, optional): 发卡支行，绑定银行必填 ,
     * realName (string, optional): 真实姓名（中文） ,
     * shortName (string, optional): 简称，ALI（支付宝），ICBC（工商银行） ,
     * stylistId (integer, optional): 理发师ID ,
     * userId (integer, optional): 用户ID
     */
    public void bindAccount(String accountNo,String bindType,String branch,
                            String realName,String shortName, String stylistId,
                            YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("accountNo", accountNo);
        params.put("bindType", bindType);
        if (branch!=null){
            params.put("branch", branch);
        }
        params.put("realName", realName);
        params.put("shortName", shortName);
        params.put("storeId", AccountManager.getInstance().getStoreId());

        mApi.bindAccount(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 获取个人账户绑定
     * @param subscriberHelper
     */
    public void extractAccount(String bindType,YLRxSubscriberHelper<CashAliResult> subscriberHelper) {
        mApi.extractAccount(AccountManager.getInstance().getUserId(),bindType)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }


    /**
     * 获取所有银行列表
     * @param subscriberHelper
     */
    public void getAllBank(YLRxSubscriberHelper<BankCardResult> subscriberHelper) {
        mApi.getAllBank()
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 解除绑定提现账户
     * @param subscriberelper
     * bindId (string, optional): 绑定账户 ,
     * userId (integer, optional): 用户ID
     */
    public void unBind(String bindId, YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("bindId", bindId);

        mApi.unBind(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }
    /**
     * 获取应用版本更新
     * @param subscriberelper
     */
    public void getAppInfos(YLRxSubscriberHelper<GetAppInfoResult> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("client", "android");
        params.put("appId", BuildConfig.APPLICATION_ID);
        params.put("appVersion", BuildConfig.VERSION_NAME);

        mApi.getAppInfos(new BaseRequestBody<>(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     *
     * @param subContent 提交内容
     * @param userId
     * @param subscriberelper
     */
    public void submitFeedback(String subContent, String userId ,YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("subContent", subContent);
        params.put("userId", userId);
        mApi.submitFeedback(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     *
     * @param userId
     * @param oldPassword 旧密码
     * @param password 新密码
     * @param nextPassword 确认密码
     * @param subscriberelper
     */
    public void upDatePwd(String userId, String oldPassword ,String password,String nextPassword ,YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("oldPassword", oldPassword);
        params.put("password", password);
        params.put("copyPassword", nextPassword);
        mApi.upDatePwd(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     *
     * @param mobile 旧手机号码
     * @param newMobile 新手机号码
     * @param type 验证码类型
     * @param vcode 验证码
     * @param subscriberelper
     */
    public void updateMobile(String mobile, String newMobile ,String type,String vcode ,YLRxSubscriberHelper<BaseResponse> subscriberelper) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("newMobile", newMobile);
        params.put("type", type);
        params.put("vcode", vcode);
        mApi.updateMobile(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberelper);
    }

    /**
     * 获取个人账户安全信息
     * @param subscriberHelper
     */
    public void getAccountSafeInfo(YLRxSubscriberHelper<SecurityInfoResult> subscriberHelper) {
        mApi.accountSafeInfo(AccountManager.getInstance().getUserId())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }


    /**
     * 绑定微信
     * @param subscriberHelper
     * @param code 微信code
     */
    public void bindWXAccount(String code,YLRxSubscriberHelper<BaseResponse> subscriberHelper) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("code", code);
        mApi.bindWXAccount(new BaseRequestBody(params).toRequestBody())
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /*
    * 验证密码
    * */
    public void checkPassword(String password,YLRxSubscriberHelper<BaseResponse> subscriberHelper){
        mApi.checkPassword(AccountManager.getInstance().getUserId(),password)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }

    /**
     * 体验反馈信息
     * @param subscriberHelper
     */
    public void initAppFeedback(YLRxSubscriberHelper<FeedBackResult> subscriberHelper) {
        mApi.initAppFeedback("android")
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(subscriberHelper);
    }


}
